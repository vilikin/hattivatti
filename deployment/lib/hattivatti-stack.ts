import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Code, Function, Runtime} from "aws-cdk-lib/aws-lambda";

export class HattivattiStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        new SpringBootLambda(this, "UpdatePriceData", {
            functionName: "hattivatti-update-price-data",
            handlerName: "updatePriceDataLambdaHandler"
        });
    }
}

interface SpringBootLambdaProps {
    functionName: string;
    handlerName: string;
}

class SpringBootLambda extends Construct {
    constructor(scope: Construct, id: string, props: SpringBootLambdaProps) {
        super(scope, id);

        new Function(this, 'UpdatePriceDataFunction', {
            functionName: props.functionName,
            memorySize: 512,
            runtime: Runtime.JAVA_11,
            code: Code.fromAsset("../lambda/build/libs/app-0.0.1-SNAPSHOT-aws.jar"),
            handler: "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest",
            environment: {
                SPRING_CLOUD_FUNCTION_DEFINITION: props.handlerName
            },
        });
    }
}
