#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { HattivattiStack } from '../lib/hattivatti-stack';

const app = new cdk.App();

new HattivattiStack(app, 'hattivatti', {
  env: { account: process.env.CDK_DEFAULT_ACCOUNT, region: process.env.CDK_DEFAULT_REGION },
});
