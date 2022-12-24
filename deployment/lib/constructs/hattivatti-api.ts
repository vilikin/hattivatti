import {Construct} from "constructs";
import {RestApi} from "aws-cdk-lib/aws-apigateway";
import {Certificate} from "aws-cdk-lib/aws-certificatemanager";
import {ARecord, HostedZone, RecordTarget} from "aws-cdk-lib/aws-route53";
import {ApiGateway} from "aws-cdk-lib/aws-route53-targets";

export class HattivattiApi extends RestApi {
    constructor(scope: Construct, id: string) {
        super(scope, id, {
            restApiName: "hattivatti-api",
            defaultMethodOptions: {
                apiKeyRequired: false
            }
        });

        const apiKey = this.addApiKey("SahkoSeppoApiKey", {
            apiKeyName: "SahkoSeppo",
            description: "API key for SähköSeppo mobile app"
        });

        const usagePlan = this.addUsagePlan("StandardUsagePlan", {
            name: "Standard"
        });

        usagePlan.addApiKey(apiKey);
        usagePlan.addApiStage({
            api: this,
            stage: this.deploymentStage
        });

        this.addDomainName("HattivattiDomain", {
            domainName: "hattivatti.link",
            certificate: Certificate.fromCertificateArn(
                this,
                "HattivattiDomainCertificate",
                "arn:aws:acm:eu-north-1:154704856875:certificate/cb73b01f-1c9d-4d10-9268-b91ee737dbd5"
            )
        });

        new ARecord(this, 'CustomDomainAliasRecord', {
            zone: HostedZone.fromHostedZoneAttributes(this, 'HattivattiHostedZone', {
                hostedZoneId: "Z04986902YX9XWLXDKF06",
                zoneName: "hattivatti.link"
            }),
            target: RecordTarget.fromAlias(new ApiGateway(this))
        });
    }
}