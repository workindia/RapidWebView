import { Stack, Box, Heading, Text, Button } from "@chakra-ui/react";
import Link from "next/link";

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
        An Android library which utilises <br />
        local caching to offer <br />
        near native experience over WebView.
      </Heading>
      <Text color={"gray.500"}>
        RapidWebView is a library for your Android application which allows you
        to load websites in a webview with a minimal TTI (Time To Interactive).
        It also provides a generic set of Javascript Interface functions which
        can let your website interact with some Android native functionalities.
      </Text>
      <Stack
        direction={"column"}
        spacing={3}
        align={"center"}
        alignSelf={"center"}
        position={"relative"}
      >
        <Link href={"/docs/getting-started"} passHref>
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
        </Link>
        <Link href={"/docs/arch"} passHref>
          <Button variant={"link"} colorScheme={"blue"} size={"sm"}>
            Learn more
          </Button>
        </Link>
      </Stack>
    </Stack>
  );
}
