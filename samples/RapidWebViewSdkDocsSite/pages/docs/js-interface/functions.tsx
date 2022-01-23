import type { NextPage } from "next";
import Head from "next/head";

import { Code, Container, Heading, Text, Link } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

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
            <Heading fontSize={"xl"} mb={"4"}>
              Javascript Interface functions
            </Heading>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default JSInterfaceFunctions;
