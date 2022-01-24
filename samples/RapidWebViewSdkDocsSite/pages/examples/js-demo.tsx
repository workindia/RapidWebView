import type { NextPage } from "next";
import Head from "next/head";

import { Container, Heading, Text, Link } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const JSDemo: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - JS Interface Demo</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            TODO
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default JSDemo;
