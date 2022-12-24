import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {Schedule} from "aws-cdk-lib/aws-events";

export interface HourlyLightStateUpdateBasedOnElectricityPriceProps {
    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class HourlyLightStateUpdateBasedOnElectricityPrice extends Construct {
    constructor(scope: Construct, id: string, props: HourlyLightStateUpdateBasedOnElectricityPriceProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "updateLightStatesBasedOnElectricityPrice",
            ...props
        });

        props.hueUsersTable.grantReadData(lambda);
        props.electricityPricesTable.grantReadData(lambda);

        lambda.runOnSchedule(
            "UpdateLightStatesBasedOnElectricityPriceEveryHour",
            Schedule.cron({
                minute: "0"
            })
        );
    }
}
