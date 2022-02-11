import type { NextPage } from "next";

import Head from "next/head";
import { Container } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import Hero from "@components/Page.Home/Hero";
import Features from "@components/Page.Home/Features";

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - SDK</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <Hero />
          <Features />
        </main>
      </Container>

      <Footer />
    </>
  );
};

export default Home;
