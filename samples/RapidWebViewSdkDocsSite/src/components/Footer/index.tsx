import { ExternalLinkIcon } from "@chakra-ui/icons";
import {
  Box,
  Container,
  Link,
  Stack,
  Text,
  useColorModeValue,
} from "@chakra-ui/react";

import Logo from "@components/Logo";

export default function SmallWithLogoLeft() {
  return (
    <Box
      bg={useColorModeValue("gray.50", "gray.900")}
      color={useColorModeValue("gray.700", "gray.200")}
    >
      <Container
        as={Stack}
        maxW={"6xl"}
        py={4}
        direction={{ base: "column", md: "row" }}
        spacing={4}
        justify={{ base: "center", md: "space-between" }}
        align={{ base: "center", md: "center" }}
      >
        <Logo withText />
        <Text>
          Made in 🇮🇳 by{" "}
          <Link color={"blue.600"} href="https://github.com/workindia">
            WorkIndia <ExternalLinkIcon mx="2px" />
          </Link>
          .
        </Text>
      </Container>
    </Box>
  );
}
