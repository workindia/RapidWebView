import type { NextPage } from "next";
import Head from "next/head";
import NLink from "next/link";

import { Container, Heading, Box, Link, Text, Code } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const FUNCTION_DOCS = [
  {
    id: 1,
    title: "Vibrate",
    description:
      "Vibrate the device. Takes vibration duration (in milliseconds) as a function parameter.",
    code: "// vibrate the device for 1 second \n.vibrate(1000);",
  },
  {
    id: 2,
    title: "Toast",
    description:
      "Show native toast. Takes text to be shown in the toast and duration (0 for short and 1 for long duration).",
    code: '.toast("Hello World", 0)',
  },
];

const FunctionDocItem = (item: {
  id: number;
  title: string;
  description: string;
  code: string;
}) => {
  return (
    <Box mb={"4"}>
      <Heading fontSize={"lg"} id={item.id.toString()} mb={"1"}>
        {item.title}
      </Heading>
      <Text fontSize={"md"} color={"gray.600"}>
        {item.description}
      </Text>
      <Code
        display={"block"}
        p={"4"}
        my={"2"}
        whiteSpace={"pre"}
        overflowX={"scroll"}
        style={{ tabSize: 2 }}
      >
        {item.code}
      </Code>
    </Box>
  );
};

const JSInterfaceFunctions: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Javascript Interface</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Box>
              <Heading fontSize={"xl"} mb={"4"}>
                Javascript Interface functions
              </Heading>

              <Text fontSize={"md"} color={"gray.600"} mb={"6"}>
                We&apos;ve developed a set of all purpose functions that you can
                use from your webapp to interact with your native application.
                We have documented them below. <br />
                Use our demo app to try out the interface functions:{" "}
                <NLink href={"/examples"} passHref>
                  <Link color={"blue.400"} textDecor={"underline"}>
                    Example
                  </Link>
                </NLink>
              </Text>
            </Box>

            <Box>
              {FUNCTION_DOCS.map((item) => (
                <FunctionDocItem key={item.id} {...item} />
              ))}
            </Box>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default JSInterfaceFunctions;
