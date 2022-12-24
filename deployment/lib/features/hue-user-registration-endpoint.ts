import {Construct} from "constructs";
import {ITable} from "aws-cdk-lib/aws-dynamodb";
import {HattivattiFunction} from "../constructs/hattivatti-function";
import {IRestApi, LambdaIntegration} from "aws-cdk-lib/aws-apigateway";

export interface HueUserRegistrationEndpointProps {
    api: IRestApi;

    electricityPricesTable: ITable;
    hueUsersTable: ITable;
}

export class HueUserRegistrationEndpoint extends Construct {
    constructor(scope: Construct, id: string, props: HueUserRegistrationEndpointProps) {
        super(scope, id);

        const lambda = new HattivattiFunction(this, "Function", {
            handlerName: "registerHueUser",
            ...props
        });

        props.hueUsersTable.grantReadWriteData(lambda);

        const hueOAuth2CallbackResource = props.api.root.addResource("hue-oauth2-callback");
        hueOAuth2CallbackResource.addMethod("GET", new LambdaIntegration(lambda));
    }
}
