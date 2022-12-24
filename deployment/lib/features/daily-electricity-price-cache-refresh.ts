import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {Schedule} from "aws-cdk-lib/aws-events";

export interface DailyElectricityPriceCacheRefreshProps {
    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class DailyElectricityPriceCacheRefresh extends Construct {
    constructor(scope: Construct, id: string, props: DailyElectricityPriceCacheRefreshProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "refreshElectricityPriceCache",
            ...props
        });

        props.electricityPricesTable.grantReadWriteData(lambda);

        lambda.runOnSchedule(
            "RefreshElectricityPriceCacheEveryDay",
            Schedule.cron({
                // Nord Pool publishes prices for next day around 11:45 UTC every day, but Entsoe API is a bit slow to
                // catch up, so we fetch new prices daily at 13:00 UTC
                hour: "13",
                minute: "00"
            })
        )
    }
}
