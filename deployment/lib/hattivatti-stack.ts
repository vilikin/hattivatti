import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {ElectricityPricesTable} from "./constructs/electricity-prices-table";
import {HueUsersTable} from "./constructs/hue-users-table";
import {HattivattiApi} from "./constructs/hattivatti-api";
import {DailyElectricityPriceCacheRefresh} from "./features/daily-electricity-price-cache-refresh";
import {HueUserRegistrationEndpoint} from "./features/hue-user-registration-endpoint";
import {WeeklyHueUserTokenRefresh} from "./features/weekly-hue-user-token-refresh";
import {ListElectricityPricesEndpoint} from "./features/list-electricity-prices-endpoint";
import {
    HourlyLightStateUpdateBasedOnElectricityPrice
} from "./features/hourly-light-state-update-based-on-electricity-price";
import {BlinkLightsInCaseOfElectricityShortage} from "./features/blink-lights-in-case-of-electricity-shortage";

export class HattivattiStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        const electricityPricesTable = new ElectricityPricesTable(this, "ElectricityPricesTable");
        const hueUsersTable = new HueUsersTable(this, "HueUsersTable");

        const tables = {
            electricityPricesTable,
            hueUsersTable
        };

        const api = new HattivattiApi(this, "HattivattiApi");

        new DailyElectricityPriceCacheRefresh(this, 'DailyElectricityPriceCacheRefresh', tables);

        new HueUserRegistrationEndpoint(this, 'HueUserRegistrationEndpoint', {
            api,
            ...tables
        });

        new WeeklyHueUserTokenRefresh(this, "WeeklyHueUserTokenRefresh", tables);

        new ListElectricityPricesEndpoint(this, "ListElectricityPricesEndpoint", {
            api,
            ...tables
        });

        new HourlyLightStateUpdateBasedOnElectricityPrice(
            this,
            "HourlyLightStateUpdateBasedOnElectricityPrice",
            tables
        );

        new BlinkLightsInCaseOfElectricityShortage(this, "BlinkLightsInCaseOfElectricityShortage", tables);
    }
}
