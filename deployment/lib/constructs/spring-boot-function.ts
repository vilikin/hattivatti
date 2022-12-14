import {Code, Function, FunctionProps, Runtime} from "aws-cdk-lib/aws-lambda";
import {Construct} from "constructs";
import {Duration} from "aws-cdk-lib";

export interface SpringBootFunctionProps extends Omit<FunctionProps, "runtime" | "code" | "handler" | "timeout"> {
    springCloudFunctionHandlerName: string;
}

export class SpringBootFunction extends Function {
    constructor(scope: Construct, id: string, props: SpringBootFunctionProps) {
        super(scope, id, {
            ...props,
            runtime: Runtime.JAVA_11,
            code: Code.fromAsset("../app/build/libs/app-0.0.1-SNAPSHOT-aws.jar"),
            handler: "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest",
            environment: {
                ...props.environment,
                SPRING_CLOUD_FUNCTION_DEFINITION: props.springCloudFunctionHandlerName
            },

            memorySize: props.memorySize ?? 1024,
            timeout: Duration.seconds(30)
        });
    }
}
