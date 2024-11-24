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
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
import RegisterModal from './RegisterModal';
import ProfileBox from './ProfileBox';

const LoginBox = () => {
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [showPassword, setShowPassword] = React.useState(false);
  const [isLoading, setIsLoading] = React.useState(false);
  const [isLoggedIn, setIsLoggedIn] = React.useState(false);
  const toast = useToast();

  // 컴포넌트 마운트 시 로그인 상태 확인
  React.useEffect(() => {
    const sessionId = localStorage.getItem('sessionId');
    if (sessionId) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('sessionId');
    setIsLoggedIn(false);
    setEmail('');
    setPassword('');
    
    // 로그아웃 이벤트 발생
    window.dispatchEvent(new Event('logout'));

    toast({
      title: '로그아웃 되었습니다',
      status: 'info',
      duration: 3000,
      isClosable: true,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!email || !password) {
      toast({
        title: '입력 오류',
        description: '이메일과 비밀번호를 모두 입력해주세요.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/v1/profile/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error(response.status === 401 
          ? '이메일 또는 비밀번호가 올바르지 않습니다.'
          : '로그인 중 오류가 발생했습니다.'
        );
      }

      const data = await response.json();

      if (!data.sessionId) {
        throw new Error('세션 ID를 받지 못했습니다.');
      }
    
      localStorage.setItem('sessionId', data.sessionId);
      setIsLoggedIn(true);

      // 로그인 이벤트 발생
      window.dispatchEvent(new Event('login'));

      toast({
        title: '로그인 성공',
        description: '환영합니다!',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      
    } catch (error) {
      toast({
        title: '로그인 실패',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleTogglePassword = () => {
    setShowPassword(!showPassword);
  };

  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  };

  const validatePassword = (password) => {
    return password.length >= 6;
  };

  // 로그인 상태에 따라 다른 컴포넌트 렌더링
  if (isLoggedIn) {
    return <ProfileBox userEmail={email} onLogout={handleLogout} />;
  }

  return (
    <Box p={6} borderWidth={1} borderColor="gray.200" borderRadius="lg" bg="white" shadow="sm">
      <VStack spacing={4} align="stretch">
        <Heading size="md" mb={2}>로그인</Heading>
        
        <form onSubmit={handleSubmit}>
          <VStack spacing={4}>
            <FormControl isRequired>
              <FormLabel>이메일</FormLabel>
              <Input
                type="email"
                placeholder="example@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                isInvalid={email && !validateEmail(email)}
              />
            </FormControl>

            <FormControl isRequired>
              <FormLabel>비밀번호</FormLabel>
              <InputGroup>
                <Input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="비밀번호를 입력하세요"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  isInvalid={password && !validatePassword(password)}
                />
                <InputRightElement>
                  <IconButton
                    icon={showPassword ? <ViewOffIcon /> : <ViewIcon />}
                    variant="ghost"
                    onClick={handleTogglePassword}
                    aria-label={showPassword ? '비밀번호 숨기기' : '비밀번호 보기'}
                  />
                </InputRightElement>
              </InputGroup>
              {password && !validatePassword(password) && (
                <Text color="red.500" fontSize="sm" mt={1}>
                  비밀번호는 최소 6자 이상이어야 합니다.
                </Text>
              )}
            </FormControl>

            <Button
              type="submit"
              colorScheme="blue"
              width="100%"
              isLoading={isLoading}
              loadingText="로그인 중..."
              isDisabled={!validateEmail(email) || !validatePassword(password)}
            >
              로그인
            </Button>
          </VStack>
        </form>

        <Divider my={4} />

        <VStack spacing={2}>
          <Text fontSize="sm" color="gray.600">
            아직 계정이 없으신가요?
          </Text>
          <RegisterModal />
        </VStack>

        <Button
          variant="link"
          size="sm"
          colorScheme="gray"
        >
          비밀번호를 잊으셨나요?
        </Button>
      </VStack>
    </Box>
  );
};

export default LoginBox;