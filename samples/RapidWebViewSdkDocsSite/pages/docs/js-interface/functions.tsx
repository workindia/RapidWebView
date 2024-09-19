import type { NextPage } from "next";
import Head from "next/head";
import NLink from "next/link";

import { Container, Heading, Box, Link, Text, Code } from "@chakra-ui/react";

import NavBar from "@components/NavBar";
import Footer from "@components/Footer";
import SidebarLayout from "@components/SidebarLayout";

const FUNCTION_DOCS = [
  {
    id: 1,
    title: "Vibrate",
    description: 
      "Vibrate the device. Takes vibration duration (in milliseconds) as a function parameter.",
    note: "Requires `android.permission.VIBRATE` to be declared in the manifest file.",
    code: "// vibrate the device for 1 second \n.vibrate(1000);",
  },
  {
    id: 2,
    title: "Toast",
    description:
      "Show native toast. Takes text to be shown in the toast and duration (0 for short and 1 for long duration).",
    code: '.toast("Hello World", 0)',
  },
  {
    id: 3,
    title: "Copy To Clipboard",
    description:
      "Copy text to the clipboard. It needs only one function parameter, the text to be copied.",
    code: '.copyToClipboard("Sample Text")',
  },
  {
    id: 4,
    title: "Get installed apps list",
    description:
      "It returns the list of package name of installed apps.",
    code: '.getInstalledAppList()',
  },
  {
    id: 5,
    title: "Start an app activity",
    description:
      "Start an activity within the application. It takes 2 parameters, activity name(class name of the activity to navigate to) and intent parameters(json of parameters to be passed to activity in string format).",
    code: '.startActivity("in.workindia.rapidwebviewandroidsample.MainActivity", "{"key1": 1, "key2": "value2"}")',
  },
  {
    id: 6,
    title: "Open Browser",
    description:
      'Open external browser as a new window. It takes two paramters, url(complete url to visit) and package name(name of browser package to open eg: "com.android.chrome").',
    code: '.openBrowserActivity("https://www.google.com/", "com.android.chrome")',
  },
  {
    id: 7,
    title: "Open Chrome Custom Tab",
    description: 'To open a chrome custom tab. It takes two parameters, url(complete url to visit) and color(Chrome custom tab color). Color in the form of string eg.) "#483D8B", "#80FFFFFF" and "red".',
    code: '.openCustomBrowserTab("https://www.google.com/", "#483D8B")',
  },
  {
    id: 8,
    title: "Place a call / Open dialer",
    description: 'Open a dialer (and call a number if permission is granted). It only requires one paramter the mobile number in the form of string.',
    note: "Requires permission `android.permission.CALL_PHONE` to place a call.",
    code: '.openDialer("1234567890")',
  },
  {
    id: 9,
    title: "Open an external intent (eg, google maps)",
    description: 'To open a new application. It requires two parameters, package name(name of package to open), and intent uri.',
    code: '.openExternalIntent("com.google.android.gms.maps", "geo:37.7749,-122.4194")',
  },
  {
    id: 10,
    title: "Open native share dialog",
    description: 'To open android\'s native share dailog. It requires only one paramter, the text to be shared.',
    code: '.openShareIntent("Try RapidWebView SDK: https://github.com/workindia/RapidWebView")',
  },
  {
    id: 11,
    title: "Share to an application (eg, slack)",
    description: 'Share text with a specific app. It requires two parameters, the package name of the app with which text is need to be shared, and second paramter the text to be shared.',
    code: '.shareToApp("com.Slack", "Checkout this SDK: https://github.com/workindia/RapidWebView")',
  },
  {
    id: 12,
    title: "Check if permissions granted (eg, android.permission.CALL_PHONE)",
    description: 'Check if permission is granted by user. It requires only one paramter, the array of permissions to check.',
    code: '.checkForPermission([ "android.permission.CALL_PHONE"])',
  },
  {
    id: 13,
    title: "Request permission",
    description: 'To ask for android permission. It requires 2 parameters, an array containing all the permissions needed, rationale Text(text to show on permission dialog), and a callback.',
    event: 'Callbacks from native can be used by using javascript event listener with the event "rapid-web-view-permission-listener" which will return object { detail: { "status" : "success","uploadUrl" : "$uploadUrl","uploadFileName" : "$fileName"} }',
    code: '.requestPermissions(["android.permission.CALL_PHONE"], "Permission required")',
  },
  {
    id: 14,
    title: "Show notification",
    description: 'To show notification to user. It requires 6 parameters, title for the notification, context text, summary text, notification icon, notification image, destination activity(activity to open when notification is clicked).',
    note: "Starting from Android 13 (SDK 33/TIRAMISU), it requires the `android.permission.POST_NOTIFICATIONS` permission.",
    code: '.showNotification("Test Notification","Test Content","Test Summary","","","in.workindia.rapidwebviewandroidsample.MainActivity"',
  },
  {
    id: 15,
    title: "Close current activity",
    description: 'Close the activity that holds the web view.',
    code: '.closeActivity()',
  },
  {
    id: 16,
    title: "Open native file upload interface",
    description: 'To upload a file using native file upload interface. It accepts 3 parameters, the file type, upload Url(where the file will be uploaded), and third a method to either "PUT" or "POST".',
    event: 'Callbacks from native can be used by using javascript event listener with the event "rapid-web-view-upload-listener" which will return object { detail: { "status" : "success","uploadUrl" : "$uploadUrl","uploadFileName" : "$fileName"} }',
    code: '.uploadFile("doc", "www.example.com/uploadfile", "PUT|POST")',
  },
  {
    id: 17,
    title: "Download a File Locally",
    description: `Downloads a file using the native DownloadManager. This function accepts three parameters: \`url\` (String), the mandatory URL of the file to download; \`fileName\` (String), an optional parameter that specifies the name of the saved file, defaulting to the last segment of the URL if not provided; and \`downloadLocation\` (String), which specifies the save location. \`downloadLocation\` can be 'EXTERNAL_FILES' (app-specific storage) or 'PUBLIC_DOWNLOADS' (default).`,
    event: "Callbacks from native can be used by using javascript event listener with the event `rapid-web-view-download-listener` which will return object `{ detail: { eventKey: \"downloadCompleted\", status: \"success|failure\", downloadId: \"downloadId\" } }`.",
    note: 'On devices running Android 9 (API level 28) or lower, ensure the WRITE_EXTERNAL_STORAGE permission is granted.',
    code: '.downLoadFileLocally("https://example_files/example.pdf","example.pdf","PUBLIC_DOWNLOADS")'
  },
  {
    id: 18,
    title: "Download and Open a File",
    description: "Downloads a file and opens it using the native DownloadManager. This function accepts three parameters: `url` (String), the mandatory URL of the file to download; `fileName` (String), an optional parameter that specifies the name of the saved file, defaulting to the last segment of the URL if not provided; and `downloadLocation` (String), which specifies the save location. `downloadLocation` can be 'EXTERNAL_FILES' (app-specific storage) or 'PUBLIC_DOWNLOADS' (default).",
    event: "Callbacks from native can be used by using javascript event listener with the event `rapid-web-view-download-listener` which will return object `{ detail: { eventKey: \"downloadCompleted|downloadUnsuccessful|packageNotFound\", status: \"success|failure\", downloadId: \"downloadId\" } }`.",
    note: "For devices running Android 9 (API level 28) or lower, ensure both WRITE_EXTERNAL_STORAGE and READ_EXTERNAL_STORAGE permissions are granted.",
    code: '.downLoadAndOpenFile("https://picsum.photos/200/300","example.png","EXTERNAL_FILES")'
  },
];

const FunctionDocItem = (item: {
  id: number;
  title: string;
  description: string;
  event?: string;
  note?: string;
  code: string;
}) => {
  return (
    <Box mb={"4"}>
      <Heading fontSize={"lg"} id={item.id.toString()} mb={"1"}>
        {item.title}
      </Heading>
      <Text fontSize={"md"} color={"gray.600"}>
        {item.description}
      </Text>
      {
        item.event && <Text marginTop={"1"} fontSize={"sm"} color={"gray.600"}>
          <strong>Events: </strong>{item.event}
        </Text>
      }
      { item.note && <Text marginTop={"1"} fontSize={"sm"} color={"gray.600"}>
          <strong>Note: </strong>{item.note}
        </Text>
      }
      <Code
        display={"block"}
        p={"4"}
        my={"2"}
        whiteSpace={"pre"}
        overflowX={"scroll"}
        style={{ tabSize: 2 }}
      >
        {item.code}
      </Code>
    </Box>
  );
};

const JSInterfaceFunctions: NextPage = () => {
  return (
    <>
      <Head>
        <title>RapidWebView - Javascript Interface</title>
      </Head>

      <Container maxW={"container.lg"}>
        <NavBar />

        <main>
          <SidebarLayout>
            <Box>
              <Heading fontSize={"xl"} mb={"4"}>
                Javascript Interface functions
              </Heading>

              <Text fontSize={"md"} color={"gray.600"} mb={"6"}>
                We&apos;ve developed a set of all purpose functions that you can
                use from your webapp to interact with your native application.
                We have documented them below. <br />
                Use our demo app to try out the interface functions:{" "}
                <NLink href={"/examples"} passHref>
                  <Link color={"blue.400"} textDecor={"underline"}>
                    Example
                  </Link>
                </NLink>
              </Text>
            </Box>

            <Box>
              {FUNCTION_DOCS.map((item) => (
                <FunctionDocItem key={item.id} {...item} />
              ))}

            </Box>
          </SidebarLayout>
        </main>
      </Container>
      <Footer />
    </>
  );
};

export default JSInterfaceFunctions;
