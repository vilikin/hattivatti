import * as cdk from 'aws-cdk-lib';
import {RemovalPolicy} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {AttributeType, BillingMode, Table} from "aws-cdk-lib/aws-dynamodb";
import {SpringBootFunction} from "./constructs/spring-boot-function";
import {StringParameter} from "aws-cdk-lib/aws-ssm";
import {Rule, Schedule} from "aws-cdk-lib/aws-events";
import {LambdaFunction} from "aws-cdk-lib/aws-events-targets";
import {LambdaIntegration, RestApi} from "aws-cdk-lib/aws-apigateway";
import {Certificate} from "aws-cdk-lib/aws-certificatemanager";

export class HattivattiStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        const hueRemoteApiBaseUrl = StringParameter.fromStringParameterName(
            this,
            "HueRemoteApiBaseUrl",
            "/hue-remote-api/base-url"
        );

        const hueRemoteApiClientId = StringParameter.fromStringParameterName(
            this,
            "HueRemoteApiClientId",
            "/hue-remote-api/client-id"
        );

        const hueRemoteApiClientSecret = StringParameter.fromStringParameterName(
            this,
            "HueRemoteApiClientSecret",
            "/hue-remote-api/client-secret"
        );

        const entsoeApiBaseUrl = StringParameter.fromStringParameterName(
            this,
            "EntsoeApiBaseUrl",
            "/entsoe-api/base-url"
        );

        const entsoeApiSecurityToken = StringParameter.fromStringParameterName(
            this,
            "EntsoeApiSecurityToken",
            "/entsoe-api/security-token"
        );

        const electricityPricesTable = new Table(this, "ElectricityPricesTable", {
            tableName: "ElectricityPrices",
            billingMode: BillingMode.PAY_PER_REQUEST,
            partitionKey: {
                name: "startTime",
                type: AttributeType.NUMBER
            },
            timeToLiveAttribute: "endTime",
            removalPolicy: RemovalPolicy.DESTROY
        });

        const hueUsersTable = new Table(this, "HueUsersTable", {
            tableName: "HueUsers",
            billingMode: BillingMode.PAY_PER_REQUEST,
            partitionKey: {
                name: "id",
                type: AttributeType.STRING
            }
        });

        const lambdaEnvironmentVariables = {
            HUE_REMOTE_API_BASE_URL: hueRemoteApiBaseUrl.stringValue,
            HUE_REMOTE_API_CLIENT_ID: hueRemoteApiClientId.stringValue,
            HUE_REMOTE_API_CLIENT_SECRET: hueRemoteApiClientSecret.stringValue,

            ENTSOE_API_BASE_URL: entsoeApiBaseUrl.stringValue,
            ENTSOE_API_SECURITY_TOKEN: entsoeApiSecurityToken.stringValue,

            ELECTRICITY_PRICES_DYNAMODB_TABLE_NAME: electricityPricesTable.tableName,
            HUE_USERS_DYNAMODB_TABLE_NAME: hueUsersTable.tableName,
        };

        const refreshElectricityPriceCacheFunction = new SpringBootFunction(
            this,
            "RefreshElectricityPriceCacheFunction",
            {
                functionName: "hattivatti-refresh-electricity-price-cache",
                springCloudFunctionHandlerName: "refreshElectricityPriceCache",
                environment: lambdaEnvironmentVariables
            }
        );

        electricityPricesTable.grantReadWriteData(refreshElectricityPriceCacheFunction);

        new Rule(this, 'RefreshElectricityPriceCacheRule', {
            ruleName: "RefreshElectricityPriceCacheEveryDay",
            schedule: Schedule.cron({
                // Nord Pool publishes prices for next day around 11:45 UTC every day, but Entsoe API is a bit slow to
                // catch up, so we fetch new prices daily at 13:00 UTC
                hour: "13",
                minute: "00"
            }),
            targets: [new LambdaFunction(refreshElectricityPriceCacheFunction)]
        });

        const registerHueUserFunction = new SpringBootFunction(
            this,
            "RegisterHueUserFunction",
            {
                functionName: "hattivatti-register-hue-user",
                springCloudFunctionHandlerName: "registerHueUser",
                environment: lambdaEnvironmentVariables
            }
        );

        hueUsersTable.grantReadWriteData(registerHueUserFunction);

        const api = new RestApi(this, "HattivattiApi", {
            restApiName: "hattivatti-api"
        });

        api.addDomainName("HattivattiDomain", {
            domainName: "hattivatti.link",
            certificate: Certificate.fromCertificateArn(
                this,
                "HattivattiDomainCertificate",
                "arn:aws:acm:eu-north-1:154704856875:certificate/cb73b01f-1c9d-4d10-9268-b91ee737dbd5"
            )
        });

        const hueOAuth2CallbackResource = api.root.addResource("hue-oauth2-callback");
        hueOAuth2CallbackResource.addMethod("POST", new LambdaIntegration(registerHueUserFunction));
    }
}
