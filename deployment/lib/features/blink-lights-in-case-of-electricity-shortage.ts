import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {Schedule} from "aws-cdk-lib/aws-events";
import {Duration} from "aws-cdk-lib";

export interface BlinkLightsInCaseOfElectricityShortageProps {
    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class BlinkLightsInCaseOfElectricityShortage extends Construct {
    constructor(scope: Construct, id: string, props: BlinkLightsInCaseOfElectricityShortageProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "blinkLightsInCaseOfElectricityShortage",
            ...props
        });

        props.hueUsersTable.grantReadData(lambda);

        lambda.runOnSchedule(
            "BlinkLightsInCaseOfElectricityShortageEvery10Minutes",
            Schedule.rate(Duration.minutes(10))
        );
    }
}
