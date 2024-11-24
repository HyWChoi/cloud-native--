import React from 'react';
import {
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  FormControl,
  FormLabel,
  Input,
  useDisclosure,
  InputGroup,
  InputRightElement,
  IconButton,
  useToast,
  VStack,
  FormErrorMessage
} from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';

const RegisterModal = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [passwordConfirm, setPasswordConfirm] = React.useState('');
  const [showPassword, setShowPassword] = React.useState(false);
  const [alias, setAlias] = React.useState('');
  const [isLoading, setIsLoading] = React.useState(false);
  const toast = useToast();

  // 유효성 검사
  const isEmailError = !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) && email !== '';
  const isPasswordError = password.length < 6 && password !== '';
  const isPasswordMatch = password === passwordConfirm;

  const handleTogglePassword = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = async () => {
    // 입력값 검증
    if (!email || !password || !passwordConfirm || !alias) {
      toast({
        title: "모든 필드를 입력해주세요",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    if (isEmailError || isPasswordError || !isPasswordMatch) {
      toast({
        title: "입력 정보를 확인해주세요",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/v1/profile/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
          email, 
          password, 
          alias
        }),
      });

      if (!response.ok) {
        throw new Error('회원가입에 실패했습니다');
      }

      const data = await response.json();
      
      toast({
        title: "회원가입 성공!",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      
      // 성공 후 모달 닫기 및 폼 초기화
      onClose();
      setEmail('');
      setPassword('');
      setPasswordConfirm('');
      setAlias('');

    } catch (error) {
      toast({
        title: "회원가입 실패",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Button width="100%" onClick={onOpen} colorScheme="blue">
        회원가입
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={onClose}
        size="md"
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader textAlign="center">회원가입</ModalHeader>
          <ModalCloseButton />
          
          <ModalBody>
            <VStack spacing={4}>
              <FormControl isInvalid={isEmailError} isRequired>
                <FormLabel>이메일</FormLabel>
                <Input
                  type="email"
                  placeholder="example@email.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                {isEmailError && (
                  <FormErrorMessage>유효한 이메일 주소를 입력해주세요</FormErrorMessage>
                )}
              </FormControl>

              <FormControl isRequired>
                <FormLabel>닉네임</FormLabel>
                <Input
                  type="text"
                  placeholder="닉네임을 입력해주세요"
                  value={alias}
                  onChange={(e) => setAlias(e.target.value)}
                />
              </FormControl>

              <FormControl isInvalid={isPasswordError} isRequired>
                <FormLabel>비밀번호</FormLabel>
                <InputGroup>
                  <Input
                    type={showPassword ? 'text' : 'password'}
                    placeholder="6자 이상 입력해주세요"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
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
                {isPasswordError && (
                  <FormErrorMessage>비밀번호는 6자 이상이어야 합니다</FormErrorMessage>
                )}
              </FormControl>

              <FormControl isInvalid={!isPasswordMatch && passwordConfirm !== ''} isRequired>
                <FormLabel>비밀번호 확인</FormLabel>
                <Input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="비밀번호를 다시 입력해주세요"
                  value={passwordConfirm}
                  onChange={(e) => setPasswordConfirm(e.target.value)}
                />
                {!isPasswordMatch && passwordConfirm !== '' && (
                  <FormErrorMessage>비밀번호가 일치하지 않습니다</FormErrorMessage>
                )}
              </FormControl>
            </VStack>
          </ModalBody>

          <ModalFooter>
            <Button 
              colorScheme="blue" 
              width="100%"
              onClick={handleSubmit}
              isLoading={isLoading}
              loadingText="처리중..."
            >
              가입하기
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
}

export default RegisterModal;