import {AttributeType, BillingMode, Table} from "aws-cdk-lib/aws-dynamodb";
import {Construct} from "constructs";
import {RemovalPolicy} from "aws-cdk-lib";

export class ElectricityPricesTable extends Table {
    constructor(scope: Construct, id: string) {
        super(scope, id, {
            tableName: "ElectricityPrices",
            billingMode: BillingMode.PAY_PER_REQUEST,
            partitionKey: {
                name: "startTime",
                type: AttributeType.NUMBER
            },
            timeToLiveAttribute: "endTime",
            removalPolicy: RemovalPolicy.DESTROY
        });
    }
}
