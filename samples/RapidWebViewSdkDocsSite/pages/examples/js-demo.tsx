import { useEffect, useState } from "react";
import type { NextPage } from "next";
import Head from "next/head";
import Link from "next/link";

import {
  Container,
  Box,
  Heading,
  Stack,
  Button,
  Code,
  SimpleGrid,
  useColorModeValue,
  Text,
  createStandaloneToast,
  useToast,
  Flex,
} from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";

const demoItems = [
  {
    id: 1,
    title: "Vibrate",
    description: "vibrate(durationMs: long)",
    link: "/docs/js-interface/functions#1",
    onClick: () => {
      // @ts-ignore
      app.vibrate(200);
    },
  },
  {
    id: 2,
    title: "Toast",
    description: "showToast(text: string, duration: int)",
    link: "/docs/js-interface/functions#2",
    onClick: () => {
      // @ts-ignore
      app.showToast("Hello World", 0);
    },
  },
  {
    id: 3,
    title: "Copy To Clipboard",
    description: "copyToClipboard(text: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.copyToClipboard("Test message copied");
    },
  },
  {
    id: 4,
    title: "Get installed apps list",
    description: "getInstalledAppList()",
    link: "#",
    onClick: () => {
      const toast = createStandaloneToast();
      toast({
        title: "Installed App List",
        // @ts-ignore
        description: app.getInstalledAppList(),
        isClosable: true,
      });
    },
  },
  {
    id: 5,
    title: "Start an app activity",
    description: "startActivity(destActivity: string, intentParams: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.startActivity(
        "in.workindia.rapidwebviewandroidsample.MainActivity",
        "{}"
      );
    },
  },
  {
    id: 6,
    title: "Open Browser",
    description: "openBrowserActivity(url: string, packageName: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.openBrowserActivity(
        "https://github.com/workindia/RapidWebView",
        "com.android.chrome"
      );
    },
  },
  {
    id: 7,
    title: "Open Chrome Custom Tab",
    description: "openCustomBrowserTab(url: string, color: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.openCustomBrowserTab(
        "https://github.com/workindia/RapidWebView",
        "#48BB78"
      );
    },
  },
  {
    id: 8,
    title: "Place a call / Open dialer",
    description: "openDialer(phoneNumber: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.openDialer("+1234567890");
    },
  },
  {
    id: 9,
    title: "Open an external intent (eg, google maps)",
    description: "openExternalIntent(packageName: string, uri: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.openExternalIntent(
        "com.google.android.apps.maps",
        "google.streetview:cbll=46.414382,10.013988"
      );
    },
  },
  {
    id: 10,
    title: "Open share intent",
    description: "openShareIntent(text: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.openShareIntent(
        "Try RapidWebView SDK: https://github.com/workindia/RapidWebView"
      );
    },
  },
  {
    id: 11,
    title: "Share to an application (eg, slack)",
    description: "shareToApp(packageName: string, shareText: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.shareToApp(
        "com.Slack",
        "Checkout this SDK: https://github.com/workindia/RapidWebView"
      );
    },
  },
  {
    id: 12,
    title: "Check if permissions granted (eg, android.permission.CALL_PHONE)",
    description: "checkForPermission(permissions: Array<string>)",
    link: "#",
    onClick: () => {
      const toast = createStandaloneToast();
      toast({
        // @ts-ignore
        title: `Permission status: ${app.checkForPermission([
          "android.permission.CALL_PHONE",
        ])}`,
        duration: 5000,
        isClosable: true,
      });
    },
  },
  {
    id: 13,
    title: "Request permission",
    description:
      "requestPermissions(permissions: Array<string>, rationaleText: string, callback: string?)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.requestPermissions(
        ["android.permission.CALL_PHONE"],
        "Permission required",
        null
      );
    },
  },
  {
    id: 14,
    title: "Show notification",
    description:
      "showNotification(title: string, contentText: string, summaryText: string?, notificationIcon: string, notificationImage: string, destActivity: string)",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.showNotification(
        "Test Notification",
        "Test Content",
        "Test Summary",
        "",
        "",
        "in.workindia.rapidwebviewandroidsample.MainActivity"
      );
    },
  },
  {
    id: 15,
    title: "Close current activity",
    description: "closeActivity()",
    link: "#",
    onClick: () => {
      // @ts-ignore
      app.closeActivity();
    },
  },
];

{
  /*
Pending examples:
- openShareIntent(shareText: String, shareImage: String)
- shareToApp(packageName: String, shareText: String, shareImage: String)
- uploadFile(fileType: String, uploadUrl: String, callback: String?)
- storeInAppCache(fileUrl: String)
- checkForFileInCache(fileUrl: String)
*/
}

const DemoItem = (props: {
  title: string;
  description: string;
  link: string;
  onClick: Function;
}) => {
  return (
    <Box
      w={"full"}
      bg={useColorModeValue("white", "gray.900")}
      boxShadow={"md"}
      rounded={"lg"}
      p={6}
      textAlign={"center"}
    >
      <Heading fontWeight={400} fontSize={"xl"}>
        {props.title}
      </Heading>
      <Code>{props.description}</Code>

      <Stack mt={8} direction={"row"} spacing={4}>
        <Link href={props.link} passHref>
          <Button
            flex={1}
            fontSize={"sm"}
            rounded={"full"}
            _focus={{
              bg: "gray.200",
            }}
          >
            Details
          </Button>
        </Link>
        <Button
          flex={1}
          fontSize={"sm"}
          rounded={"full"}
          colorScheme={"blue"}
          boxShadow={
            "0px 1px 25px -5px rgb(66 153 225 / 48%), 0 10px 10px -5px rgb(66 153 225 / 43%)"
          }
          onClick={() => {
            jsCompatible() && props.onClick();
          }}
        >
          Try me!
        </Button>
      </Stack>
    </Box>
  );
};

const jsCompatible = (): boolean => {
  // @ts-ignore
  return typeof app === "object";
};

const JSDemo: NextPage = () => {
  const [jsInterfaceState, setJsInterfaceState] = useState(false);

  useEffect(() => {
    setJsInterfaceState(jsCompatible());
  }, []);

  return (
    <>
      <Head>
        <title>RapidWebView - JS Interface Demo</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <Box py={"6"}>
            <Heading textAlign={"center"} size={"md"} mb={"6"}>
              RapidWebView JS Interface Demo
            </Heading>

            {!jsInterfaceState ? (
              <Flex
                alignItems={"center"}
                backgroundColor={"red.500"}
                px={"3"}
                py={"2"}
                mb={"6"}
                borderRadius={"lg"}
              >
                <Box flex={1}>
                  <Text color={"white"} fontWeight={"bold"}>
                    The demo only works on the sample app
                  </Text>
                </Box>
                <Link href={"/examples"} passHref>
                  <Button colorScheme={"green"} size={"sm"}>
                    Download
                  </Button>
                </Link>
              </Flex>
            ) : (
              <Box></Box>
            )}

            <SimpleGrid columns={1} spacingY={"3"}>
              {demoItems.map((item) => (
                <DemoItem key={item.id} {...item}></DemoItem>
              ))}
            </SimpleGrid>
          </Box>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default JSDemo;
