import React from "react";

import NLink from "next/link";
import { useRouter } from "next/router";

import {
  Box,
  Flex,
  Heading,
  Link,
  Text,
  IconButton,
  useDisclosure,
} from "@chakra-ui/react";

import { HamburgerIcon, CloseIcon } from "@chakra-ui/icons";

const sideBarItems = [
  {
    id: 1,
    type: "header",
    title: "Getting Started",
    href: "",
  },
  {
    id: 2,
    type: "item",
    title: "Android SDK Installation",
    href: "/docs/getting-started",
  },
  {
    id: 3,
    type: "item",
    title: "Webapp Setup",
    href: "/docs/getting-started/webapp",
  },
  {
    id: 4,
    type: "header",
    title: "Understanding the architecture",
    href: "",
  },
  {
    id: 5,
    type: "item",
    title: "Overview",
    href: "/docs/arch",
  },
  {
    id: 6,
    type: "item",
    title: "Asset Downloader",
    href: "/docs/arch/#asset-downloader",
  },
  {
    id: 7,
    type: "item",
    title: "WebView Request Interceptor",
    href: "/docs/arch/#request-interceptor",
  },
  {
    id: 8,
    type: "header",
    title: "Javascript Interface",
    href: "",
  },
  {
    id: 9,
    type: "item",
    title: "Functions",
    href: "/docs/js-interface/functions",
  },
];

export default function SidebarLayout(props: React.PropsWithChildren<{}>) {
  const { isOpen, onToggle } = useDisclosure();
  const { asPath, pathname } = useRouter();

  let currentPageItem = sideBarItems.filter(
    (item) => item.type === "item" && item.href === pathname
  );
  const currentPage =
    currentPageItem.length == 1 ? currentPageItem[0] : { id: -1, title: "" };

  return (
    <>
      <Flex flexDirection={{ base: "column", md: "row" }}>
        {/* Sidebar + Mobile UI */}
        <Flex
          display={{ base: "flex", md: "none" }}
          width={"100%"}
          my={"3"}
          px={"4"}
          py={"3"}
          alignItems={"center"}
          backgroundColor={"gray.50"}
          borderRadius={"md"}
          onClick={onToggle}
        >
          <Text fontSize={"md"} flex={"1"}>
            {currentPage.title}
          </Text>

          <IconButton
            icon={
              isOpen ? <CloseIcon w={3} h={3} /> : <HamburgerIcon w={5} h={5} />
            }
            variant={"ghost"}
            aria-label={"Toggle Sidebar"}
          />
        </Flex>

        <Box
          as={"nav"}
          display={isOpen ? "block" : { base: "none", md: "block" }}
          py={4}
          pr={8}
          pl={6}
          maxWidth={{ base: "100%", md: "240px" }}
          minHeight={"calc(100vh - 4rem)"}
          flex={"2"}
        >
          {sideBarItems.map((item) =>
            item.type == "header" ? (
              <Heading
                key={item.id}
                as={"h4"}
                fontSize={"sm"}
                pt={item.id === 1 ? "1" : "6"}
                pb={"1"}
                px={"3"}
              >
                {item.title}
              </Heading>
            ) : (
              <Flex key={item.id} mt={"2"}>
                <NLink href={item.href} passHref>
                  <Link
                    fontSize={"sm"}
                    fontWeight={"500"}
                    color={currentPage.id == item.id ? "blue.600" : "gray.700"}
                    width={"100%"}
                    py={"1"}
                    px={"3"}
                    borderRadius={"sm"}
                    backgroundColor={
                      currentPage.id == item.id ? "gray.50" : "inherit"
                    }
                  >
                    {item.title}
                  </Link>
                </NLink>
              </Flex>
            )
          )}
        </Box>

        {/* Content */}
        <Box display={isOpen ? "none" : "block"} px={"4"} py={"5"} flex={"1"}>
          {props.children}
        </Box>
      </Flex>
    </>
  );
}
