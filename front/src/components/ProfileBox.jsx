import React from 'react';
import {
  Box,
  Heading,
  VStack,
  FormControl,
  FormLabel,
  Input,
  Button,
  Text,
  InputGroup,
  InputRightElement,
  IconButton,
  useToast,
  Divider,
  Avatar,
  Flex,
  Spacer
} from '@chakra-ui/react';

const ProfileBox = ({ userEmail, onLogout }) => {
  return (
    <Box p={6} borderWidth={1} borderColor="gray.200" borderRadius="lg" bg="white" shadow="sm">
      <VStack spacing={4} align="stretch">
        <Flex align="center">
          <Avatar size="lg" name={userEmail} src="https://bit.ly/broken-link" />
          <Box ml={4}>
            <Heading size="md">{userEmail}</Heading>
            <Text color="gray.600">프로필</Text>
          </Box>
          <Spacer />
        </Flex>
        <Button colorScheme="red" onClick={onLogout}>로그아웃</Button>
      </VStack>
    </Box>
  );
};

export default ProfileBox;