import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {Schedule} from "aws-cdk-lib/aws-events";
import {Duration} from "aws-cdk-lib";

export interface WeeklyHueUserTokenRefreshProps {
    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class WeeklyHueUserTokenRefresh extends Construct {
    constructor(scope: Construct, id: string, props: WeeklyHueUserTokenRefreshProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "refreshAllHueUserTokens",
            ...props
        });

        props.hueUsersTable.grantReadWriteData(lambda);

        lambda.runOnSchedule(
            "RefreshAllHueUserTokensEvery6Days",
            Schedule.rate(Duration.days(6))
        )
    }
}
