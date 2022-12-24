import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {SpringBootFunction} from "./spring-boot-function";
import * as _ from "lodash";
import {Rule, Schedule} from "aws-cdk-lib/aws-events";
import {LambdaFunction} from "aws-cdk-lib/aws-events-targets";
import {StringParameter} from "aws-cdk-lib/aws-ssm";

export interface HattivattiFunctionProps {
    handlerName: string;
    electricityPricesTable: ITable;
    hueUsersTable: ITable
}

export class HattivattiFunction extends SpringBootFunction {
    constructor(scope: Construct, id: string, props: HattivattiFunctionProps) {
        super(scope, id, {
            functionName: `hattivatti-${_.kebabCase(props.handlerName)}`,
            springCloudFunctionHandlerName: props.handlerName,
            environment: {
                HUE_REMOTE_API_BASE_URL: StringParameter.valueForStringParameter(scope, "/hue-remote-api/base-url"),
                HUE_REMOTE_API_CLIENT_ID: StringParameter.valueForStringParameter(scope, "/hue-remote-api/client-id"),
                HUE_REMOTE_API_CLIENT_SECRET: StringParameter.valueForStringParameter(scope, "/hue-remote-api/client-secret"),

                ENTSOE_API_BASE_URL: StringParameter.valueForStringParameter(scope, "/entsoe-api/base-url"),
                ENTSOE_API_SECURITY_TOKEN: StringParameter.valueForStringParameter(scope, "/entsoe-api/security-token"),

                ELECTRICITY_PRICES_DYNAMODB_TABLE_NAME: props.electricityPricesTable.tableName,
                HUE_USERS_DYNAMODB_TABLE_NAME: props.hueUsersTable.tableName,
            }
        });
    }

    public runOnSchedule = (ruleName: string, schedule: Schedule) => {
        new Rule(this, `${ruleName}Rule`, {
            ruleName,
            schedule,
            targets: [new LambdaFunction(this)]
        });
    }
}
