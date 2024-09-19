import type { NextPage } from "next";
import Head from "next/head";
import NLink from "next/link";

import { Code, Container, Heading, Text, Link } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const GettingStarted: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Getting Started (Android)</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Heading fontSize={"xl"} mb={"4"}>
              Android SDK Installation
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"6"}>
              This guide explains how you can integrate RapidWebView SDK in to
              your Android Application.
            </Text>

            <Heading fontSize={"lg"} mb={"3"}>
              Dependency
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Begin by adding Jitpack to your root <Code>build.gradle</Code>
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  "allprojects { \n\trepositories {\n\t\t...\n\t\tmaven { url 'https://jitpack.io' }\n\t}\n}"
                }
              </Code>
            </Text>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Add sdk to your app&apos;s <Code>build.gradle</Code>
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  "dependencies {\n\t...\n\timplementation 'com.github.workindia:RapidWebView:[latest-version]'\n}"
                }
              </Code>
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Initialising RapidClient
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Initialise RapidClient in <Code>StartApplication</Code> of your
              application
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  "import in.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader;\n\n...\nRapidClient.createInstance(this);                  "
                }
              </Code>
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Initialising RapidAssetCacheDownloader
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Initialise the asset cache downloader by passing it url to
              asset-manifest.json (
              <Link
                href="/docs/getting-started/webapp"
                target={"_blank"}
                color={"blue.600"}
              >
                Read more about the manifest here
              </Link>
              ). The downloader also accepts retry-count as second parameter which  
              will be used to determine how many times the downloader will retry 
              in case of an error. Asset cache initialisation needs to be done 
              soon after the application opens, before the webview is loaded. 
              The operation does not run on UI thread and will not impact the users
              experience.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  'import in.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader;\n\n...\nRapidAssetCacheDownloader.initialise(\n\t"https://rapid-web-view.netlify.app/_next/static/assets-manifest.json",\n\t5\n);'
                }
              </Code>
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Setting up a webview with RapidWebViewClient
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              Add RapidWebViewClient to a webview in your app. This client
              intercepts requests made by the webview and responds with a local
              asset copy if it&apos;s available.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  'import in.workindia.rapidwebview.RapidWebViewClient;\n\n...\nprivate WebView mWebView;\n...\n\n// Initialise mWebView and add WebSettings of your choice\n\nmWebView.setWebViewClient(new RapidWebViewClient());\nmWebView.loadUrl("https://rapid-web-view.netlify.app/");'
                }
              </Code>
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Adding the RapidWebViewJSInterface{" "}
              <Text
                fontSize={"md"}
                fontWeight={"400"}
                color={"gray.600"}
                display={"inline-flex"}
              >
                (optional)
              </Text>
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              We have included a Javascript interface with a few generic
              use-cases which interacts with native android functionality.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  'import in.workindia.rapidwebview.RapidWebViewJSInterface;\n\n...\nmWebView.addJavascriptInterface(\n\tnew RapidWebViewJSInterface(this, this, mWebView), "app"\n);'
                }
              </Code>
              You can read more about the Js Interface functions{" "}
              <NLink href={"/docs/js-interface/functions"} passHref>
                <Link color={"blue.600"} textDecor={"underline"}>
                  here
                </Link>
              </NLink>
            </Text>

            <Heading fontSize={"lg"} mb={"3"} mt={"6"}>
              Permissions{" "}
              <Text
                fontSize={"md"}
                fontWeight={"400"}
                color={"gray.600"}
                display={"inline-flex"}
              >
                (optional)
              </Text>
            </Heading>

            <Text fontSize={"md"} color={"gray.600"} mb={"3"}>
              In order to support certain functionalities within JavascriptInterface, you may add following permissions to your AndroidManifest.xml.
              <Code
                display={"block"}
                p={"4"}
                my={"2"}
                whiteSpace={"pre"}
                overflowX={"scroll"}
                style={{ tabSize: 2 }}
              >
                {
                  ` <uses-permission android:name="android.permission.CALL_PHONE" /> \n <uses-permission android:name="android.permission.VIBRATE" /> \n <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />  \n <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />  \n <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />`
                }
              </Code>
            </Text>
            <Text fontSize={"lg"} mt={"6"} mb={"4"}>
              Next: Setup a web app which uses WebPack.{" "}
              <NLink href={"/docs/getting-started/webapp"}>
                <Link color={"blue.600"}>Webapp Setup</Link>
              </NLink>
            </Text>
          </SidebarLayout>
        </main>
      </Container>

      <Footer />
    </>
  );
};

export default GettingStarted;
