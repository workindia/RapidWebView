import type { NextPage } from "next";
import Head from "next/head";

import {
  Container,
  Heading,
  Box,
  Text,
  Link,
  UnorderedList,
  ListItem,
  Code,
} from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";
import Image from "next/image";
import NLink from "next/link";

const Architecture: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Understanding the architecture (Overview)</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Heading fontSize={"xl"} mb={"4"}>
              Understanding the architecture
            </Heading>

            <Box mb={"6"}>
              <Text fontSize={"md"} mb={"2"}>
                RapidWebView SDK uses two caching concepts from{" "}
                <Link
                  href="https://web.dev/offline-cookbook"
                  color={"blue.600"}
                  textDecor={"underline"}
                >
                  The offline cookbook
                </Link>
                :
              </Text>
              <UnorderedList>
                <ListItem>
                  <Link
                    href="https://web.dev/offline-cookbook/#on-install-as-dependency"
                    color={"blue.600"}
                    textDecor={"underline"}
                  >
                    cache on install (or app open in this case)
                  </Link>
                  , for static components (Javascript, CSS, Images)
                </ListItem>
                <ListItem>
                  <Link
                    href="https://web.dev/offline-cookbook/#on-install-as-dependency"
                    color={"blue.600"}
                    textDecor={"underline"}
                  >
                    cache, falling back to network
                  </Link>
                  , for all requests
                </ListItem>
              </UnorderedList>
              <Image
                src={"/images/arch-caching.png"}
                alt="caching strategies"
                width={"1120"}
                height={"1018"}
              />
              <Text fontSize={"md"}>
                RapidWebView SDK is a combination of 3 components: The asset
                downloader, The request interceptor and The Javascript
                Interface.
              </Text>
            </Box>

            <Box id={"asset-downloader"} mb={"4"}>
              <Heading fontSize={"lg"} mb={"2"}>
                Asset Downloader
              </Heading>
              <Text mb={"3"}>
                The asset downloader is responsible for caching specified static
                asset files which belong to the web-apps that you would load in
                your web view. The list of asset files to be cached on app
                launch are mentioned in the asset-manifest file (you can read
                the details about the asset-manifest{" "}
                <NLink href={"/docs/getting-started/webapp"} passHref>
                  <Link color={"blue.600"} textDecor={"underline"}>
                    here
                  </Link>
                </NLink>
                ).
              </Text>
              <Text mb={"3"}>
                Upon initialisation, the asset downloader uses{" "}
                <Link
                  href="https://square.github.io/retrofit/"
                  textDecor={"underline"}
                  isExternal
                >
                  retrofit
                </Link>{" "}
                to fetch the remote asset-manifest. It reads the{" "}
                <Code>version</Code> and matches it against the local cache
                version. If the remote version and the local versions match, the
                asset downloader finishes it&apos;s process. If the remote
                version and local versions are different, the asset downloader
                starts a background task which clears the local cache and starts
                downloading individual asset files mentioned in the
                asset-manifest. Once all asset files are downloaded, the asset
                downloader updates the local cache version and saves it on the
                local disk.
              </Text>
              <Text mb={"3"}>
                We recommend that you execute asset downloader on each app
                launch so that the cached assets are always updated to the
                latest version.
              </Text>
            </Box>

            <Box id={"request-interceptor"} mb={"4"}>
              <Heading fontSize={"lg"} mb={"2"}>
                Request Interceptor
              </Heading>
              <Text mb={"3"}>
                Request Interceptor is a Client class which inherits{" "}
                <Code>android.webkit.WebViewClient</Code> and overrides{" "}
                <Code>shouldInterceptRequest</Code>. It intercepts each fetch
                request made by the webview while loading the web-app and
                returns asset data from the local cache disk if it matches the
                requested resource. If a local cache copy is found, the request
                will be fulfilled by the network.
              </Text>
              <Text mb={"3"}>
                By servicing assets locally, web-app load times are reduced
                significantly.
              </Text>
            </Box>

            <Box mb={"4"}>
              <Heading fontSize={"lg"} mb={"2"}>
                Javascript Interface
              </Heading>
              <Text mb={"3"}>
                This is an add-on module included in the RapidWebView-SDK.
                It&apos;s a collection of commonly used interface functions
                which can be used by your web-app (loaded via webview) to
                interact with the native functions of Android (e.g., vibration,
                notification). You can find more details{" "}
                <NLink href={"/docs/js-interface/functions"} passHref>
                  <Link color={"blue.600"} textDecor={"underline"}>
                    here
                  </Link>
                </NLink>
                .
              </Text>
            </Box>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default Architecture;
