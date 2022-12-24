import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {IRestApi, LambdaIntegration} from "aws-cdk-lib/aws-apigateway";

export interface ListElectricityPricesEndpointProps {
    api: IRestApi;

    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class ListElectricityPricesEndpoint extends Construct {
    constructor(scope: Construct, id: string, props: ListElectricityPricesEndpointProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "listElectricityPrices",
            ...props
        });

        props.electricityPricesTable.grantReadData(lambda);

        const electricityPricesResource = props.api.root.addResource("electricity-prices");
        electricityPricesResource.addMethod(
            "GET",
            new LambdaIntegration(lambda),
            {
                apiKeyRequired: true
            }
        );
    }
}
