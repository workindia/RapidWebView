import { Text, Box, useColorModeValue } from "@chakra-ui/react";
import Image from "next/image";

export default function Logo(props: any) {
  return (
    <Box
      display={"flex"}
      alignItems={"center"}
      flexDir={"row"}
      color={useColorModeValue("gray.800", "white")}
    >
      <Image src={"/logo.svg"} alt="logo" width={42} height={42} />
      {props.withText ? (
        <Text pl={"1"} fontWeight={"bold"}>
          RapidWebView
        </Text>
      ) : (
        ""
      )}
    </Box>
  );
}
