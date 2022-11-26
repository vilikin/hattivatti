import * as cdk from 'aws-cdk-lib';
import {RemovalPolicy} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {AttributeType, BillingMode, Table} from "aws-cdk-lib/aws-dynamodb";
import {SpringBootFunction} from "./constructs/spring-boot-function";
import {StringParameter} from "aws-cdk-lib/aws-ssm";
import {Rule, Schedule} from "aws-cdk-lib/aws-events";
import {LambdaFunction} from "aws-cdk-lib/aws-events-targets";

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

        const lambdaEnvironmentVariables = {
            HUE_REMOTE_API_BASE_URL: hueRemoteApiBaseUrl.stringValue,
            HUE_REMOTE_API_CLIENT_ID: hueRemoteApiClientId.stringValue,
            HUE_REMOTE_API_CLIENT_SECRET: hueRemoteApiClientSecret.stringValue,

            ENTSOE_API_BASE_URL: entsoeApiBaseUrl.stringValue,
            ENTSOE_API_SECURITY_TOKEN: entsoeApiSecurityToken.stringValue,

            ELECTRICITY_PRICES_DYNAMODB_TABLE_NAME: electricityPricesTable.tableName
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
    }
}
