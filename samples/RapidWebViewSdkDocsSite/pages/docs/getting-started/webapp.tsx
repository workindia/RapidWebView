import type { NextPage } from "next";
import Head from "next/head";

import {
  Code,
  Container,
  Heading,
  Text,
  Link,
  UnorderedList,
  ListItem,
  Box,
  Divider,
  Alert,
  AlertIcon,
} from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const GettingStartedWebapp: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Getting Started (Webapp)</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Heading fontSize={"xl"} mb={"4"}>
              Setting up a Webapp
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              This guide explains how you can set up a webapp to be used along
              with RapidWebView SDK.
            </Text>
            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              The webapp needs to use{" "}
              <Link href="https://webpack.js.org/" textDecor={"underline"}>
                Webpack
              </Link>
              , a Javascript bundler. Webpack analyses all modules in your app,
              creates a dependency graph, compiles them together in an optimized
              way in one or more bundle files which your{" "}
              <Code>index.html</Code> can reference. By utilising a feature
              called code-splitting, webpack splits your code into smaller
              chunks which can be loaded on demand or in parallel.
            </Text>
            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              In the process of building an app using webpack, we generate a{" "}
              <Link
                href="https://webpack.js.org/concepts/manifest/"
                textDecor={"underline"}
              >
                manifest
              </Link>{" "}
              which is a source map of all compiled assets (Javascript, CSS,
              Images, Fonts, etc.).
            </Text>
            <Text fontSize={"md"} color={"gray.600"} mb={"6"}>
              At its core, RapidWebView SDK reads this manifest and caches
              in Android local directory so that it can be referenced later by
              the Webview interceptor.
            </Text>

            <Text fontSize={"md"} mb={"3"}>
              In this guide we will show an example webapp written using{" "}
              <Link href="https://nextjs.org/" textDecor={"underline"}>
                NextJS
              </Link>{" "}
              framework. The guide can be applied to ReactJS, VueJS and other
              Javascript frameworks as well.
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Manifest Format
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              RapidWebView requires the manifest to be formatted like below.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  '{\n\t"version": "1.0",\n\t"asset_urls": [\n\t\t"https://example.com/static/assets/1.js",\n\t\t"https://example.com/static/assets/2.js",\n\t\t"https://example.com/static/assets/1.css"\n\t]\n}'
                }
              </Code>
              • <Code>version</Code> is a string field which is compared with
              the local cache version by the SDK. On version mismatch,
              RapidWebView clears the local cache &amp; downloads the new
              assets. <br />• <Code>asset_urls</Code> is an array of assets that
              are to be cached by the SDK.
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Setting up webpack to generate Asset Manifest
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              We will be using{" "}
              <Link
                href="https://github.com/webdeveric/next-assets-manifest"
                textDecor={"underline"}
              >
                webdeveric/next-assets-manifest
              </Link>{" "}
              to generate manifest.json for our NextJS 12 app.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {"npm i -D next-assets-manifest"}
              </Code>
            </Text>
            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Making the required changes in <Code>next.config.js</Code>
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  "const withAssetsManifest = require('next-assets-manifest');\n\n"
                }
                {
                  "// Loading packageJson to fetch app version\nconst packageJson = require('./package.json');\n\n"
                }
                {
                  "// Setting public path where the assets are hosted\nPUBLIC_PATH = 'https://rapid-web-view.netlify.app/_next/';\n\n"
                }
                {"module.exports = withAssetsManifest({\n"}
                {"\tassetsManifest: {\n"}
                {"\t\toutput: 'static/assets-manifest.json',\n"}
                {"\t\ttransform(assets, manifest) {\n"}
                {"\t\t\tconst items = Object(null)\n"}
                {"\t\t\tconst processedAssets = [];\n\n"}
                {"\t\t\tObject.keys(assets).forEach(key => {\n"}
                {'\t\t\t\tif (!assets[key].includes("json")) {\n'}
                {
                  "\t\t\t\t\tprocessedAssets.push((`${PUBLIC_PATH}${assets[key]}`));\n"
                }
                {"\t\t\t\t}\n"}
                {"\t\t\t})\n\n"}
                {"\t\t\titems['version'] = packageJson.version\n"}
                {"\t\t\titems['asset_urls'] = processedAssets;\n\n"}
                {"\t\t\treturn items;\n"}
                {"\t\t},\n"}
                {"});\n"}
              </Code>
              • You can read about asset-manifest configuration options{" "}
              <Link
                href="https://github.com/webdeveric/webpack-assets-manifest#options-read-the-schema"
                color={"blue.600"}
                textDecor={"underline"}
              >
                here.
              </Link>
              <br />• <Code>assetManifest.output</Code> is setting the path
              where the manifest json will be written. For next-js{" "}
              <Code>static/</Code> directory is publically accessible and hence
              we&apos;ve used it to host our asset manifest. You can choose to
              host and serve the manifest according to your use-case.
              <br />• <Code>assetManifest.transform</Code> is a callback which
              allows us to format the manifest json in required format.
            </Text>

            <Text fontSize={"md"}>
              Running <Code>npm run build</Code> generates the final app bundle
              and corresponding manifest which is then deployed on a server. If
              you followed the guide above, the asset manifest should be
              accessible by the SDK at{" "}
              <Code>domain/_next/static/assets-manifest.json</Code>
            </Text>

            <Divider mt={"6"} />

            <Text fontSize={"md"} mb={"6"} mt={"3"}>
              This guide is written for NextJS. A considerable amount of changes
              can be required to the manifest building process if you are using
              a different framework. We will try to document the process for
              popular JS framework in future. If you want to write a guide for a
              framework of your choice, send an{" "}
              <Link
                textDecor={"underline"}
                target={"_blank"}
                href="mailto:kshitij.nagvekar@workindia.in"
              >
                email
              </Link>
              .
            </Text>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default GettingStartedWebapp;
