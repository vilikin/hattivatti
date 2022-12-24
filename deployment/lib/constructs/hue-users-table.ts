import {AttributeType, BillingMode, Table} from "aws-cdk-lib/aws-dynamodb";
import {Construct} from "constructs";

export class HueUsersTable extends Table {
    constructor(scope: Construct, id: string) {
        super(scope, id, {
            tableName: "HueUsers",
            billingMode: BillingMode.PAY_PER_REQUEST,
            partitionKey: {
                name: "id",
                type: AttributeType.STRING
            }
        });
    }
}
