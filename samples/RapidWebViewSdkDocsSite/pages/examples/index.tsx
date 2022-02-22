import type { NextPage } from "next";
import Head from "next/head";
import NLink from "next/link";

import { Container, Box, Text, Link, Button, Divider } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const Examples: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Examples</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Box mb={"6"}>
              <Text fontSize={"lg"} mb={"3"}>
                Get the sample android application
              </Text>
              <Button
                as="a"
                href="https://appsenjoy.com/rwWe7"
                target={"_blank"}
                colorScheme={"green"}
                mr="3"
              >
                Download
              </Button>
              <Button
                as="a"
                href="https://github.com/workindia/RapidWebView/tree/master/samples/sampleApp/RapidWebViewAndroidSample"
                colorScheme={"gray"}
              >
                Github
              </Button>
            </Box>

            <Divider />

            <Box mb={"6"} mt={"6"}>
              <Text fontSize={"lg"} mb={"3"}>
                Javascript interface demo (Only works with sample app above)
              </Text>
              <NLink href={"/examples/js-demo"}><Button colorScheme={"blue"}>Visit</Button></NLink>
            </Box>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default Examples;
