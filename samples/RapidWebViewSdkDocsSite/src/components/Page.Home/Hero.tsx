import { Stack, Box, Heading, Text, Button, Link } from "@chakra-ui/react";
import NLink from "next/link";

export default function Hero() {
  return (
    <Stack
      as={Box}
      textAlign={"center"}
      spacing={{ base: 8, md: 14 }}
      py={{ base: 20, md: 36 }}
    >
      <Heading
        fontWeight={600}
        fontSize={{ base: "xl", sm: "2xl", md: "4xl" }}
        lineHeight={"110%"}
      >
        Native experience on WebView
        <br />
        made possible
      </Heading>
      <Text color={"gray.500"}>
        Introducing RapidWebView - an open source library for your Android apps
        for loading websites in a webview with minimal TTI (Time To
        Interactive). That&apos;s not all, RapidWebView Javascript client comes
        with a{" "}
        <NLink href={"/docs/js-interface/functions"} passHref>
          <Link color={"blue.600"} textDecor={"underline"}>
            host of functionalities
          </Link>
        </NLink>{" "}
        to make your webview interactive blurring the lines between webview and
        native even further! It&apos;s easy, it&apos;s fast, and it&apos;s free.
      </Text>
      <Stack
        direction={"column"}
        spacing={3}
        align={"center"}
        alignSelf={"center"}
        position={"relative"}
      >
        <NLink href={"/docs/getting-started"} passHref>
          <Button
            colorScheme={"blue"}
            bg={"blue.600"}
            rounded={"full"}
            px={6}
            _hover={{
              bg: "blue.700",
            }}
          >
            Get Started
          </Button>
        </NLink>
        <NLink href={"/docs/arch"} passHref>
          <Button variant={"link"} colorScheme={"blue"} size={"sm"}>
            How does it work?
          </Button>
        </NLink>
      </Stack>
    </Stack>
  );
}
