import { CheckCircleIcon } from "@chakra-ui/icons";
import {
  Container,
  Box,
  Stack,
  List,
  ListItem,
  ListIcon,
  Heading,
  Text,
  Code,
} from "@chakra-ui/react";

import { DownloadIcon, ViewIcon, SettingsIcon } from "@chakra-ui/icons";

import Image from "next/image";

const features = [
  {
    id: 1,
    title: "Rapid Asset Cache",
    icon: DownloadIcon,
    text: "Keeps a local copy of webapp assets provided by you during initialisation. Supports asset versioning as well.",
  },
  {
    id: 2,
    title: "WebView Request Interceptor",
    icon: ViewIcon,
    text: 'Intercepts requests made by webview and serves assets from local copy reducing the website\'s "Time to interactive"',
  },
  {
    id: 3,
    title: "Javascript Interface",
    icon: SettingsIcon,
    text: "Provides a generic collection of javascript interface functions which can be used to interactive with the native application",
  },
];

export default function Features() {
  return (
    <Box p={4}>
      <Stack
        spacing={4}
        as={Container}
        maxW={"3xl"}
        textAlign={"center"}
        mb={"8"}
      >
        <Heading fontSize={"3xl"}>Performance</Heading>
        <Text color={"gray.600"} fontSize={"md"} pb={"4"}>
          We enabled webview content debugging
          <Code display={{ base: "none", md: "inline-block" }}>
            setWebContentsDebuggingEnabled(true)
          </Code>{" "}
          and used Lighthouse from Chrome DevTools to measure performance of the
          pages loaded with and without RapidWebView SDK. The results can be
          seen below.{" "}
          <Text color={"green.400"} display={"inline-flex"}>
            A 67.2% improvement in TTI!
          </Text>
        </Text>

        <Image
          src={"/benchmark.png"}
          alt="benchmark"
          width={836}
          height={468}
        />
      </Stack>

      <Stack spacing={4} as={Container} maxW={"3xl"} mb={"8"}>
        <Heading fontSize={"3xl"} textAlign={"center"}>
          Features
        </Heading>

        <List spacing={3}>
          {features.map((feature) => (
            <ListItem key={feature.id}>
              <ListIcon as={feature.icon} color="blue.600" />
              <Text display={"inline-flex"} fontWeight={600}>
                {feature.title}
              </Text>
              <Text color={"gray.600"}>{feature.text}</Text>
            </ListItem>
          ))}
        </List>
      </Stack>
    </Box>
  );
}
