import type { NextPage } from "next";
import Head from "next/head";

import { Code, Container, Heading, Text, Link } from "@chakra-ui/react";

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
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default GettingStartedWebapp;
