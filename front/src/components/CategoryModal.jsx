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
  useToast,
  FormErrorMessage
} from '@chakra-ui/react';

const CategoryModal = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [content, setContent] = React.useState('');
  const [isLoading, setIsLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const toast = useToast();
  const initialRef = React.useRef();
  const finalRef = React.useRef();

  const resetForm = () => {
    setContent('');
    setError('');
  };

  const handleClose = () => {
    resetForm();
    onClose();
  };

  const handleSubmit = async () => {
    const sessionId = localStorage.getItem('sessionId');
    
    if (!content.trim()) {
      setError('카테고리 이름을 입력해주세요');
      return;
    }
  
    if (!sessionId) {
      toast({
        title: '로그인 필요',
        description: '로그인 후 이용해주세요.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      return;
    }
  
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/v1/categories/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-Session-ID': sessionId
        },
        body: JSON.stringify({ content: content.trim() })
      });
  
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || '카테고리 생성에 실패했습니다.');
      }
  
      const data = await response.json();
      
      toast({
        title: '카테고리 생성 성공',
        description: '새로운 카테고리가 추가되었습니다.',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
  
      handleClose();
    } catch (error) {
      console.error('Category creation error:', error);
      toast({
        title: '카테고리 생성 실패',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Button width="100%" onClick={onOpen} colorScheme="blue">
        카테고리 추가
      </Button>

      <Modal
        initialFocusRef={initialRef}
        finalFocusRef={finalRef}
        isOpen={isOpen}
        onClose={handleClose}
        isCentered
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>카테고리 추가</ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            <FormControl isInvalid={!!error}>
              <FormLabel>카테고리 이름</FormLabel>
              <Input
                ref={initialRef}
                placeholder="예: 식비, 교통비, 쇼핑 등"
                value={content}
                onChange={(e) => {
                  setContent(e.target.value);
                  setError('');
                }}
                onKeyPress={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault();
                    handleSubmit();
                  }
                }}
              />
              {error && <FormErrorMessage>{error}</FormErrorMessage>}
            </FormControl>
          </ModalBody>

          <ModalFooter>
            <Button
              colorScheme="blue"
              mr={3}
              onClick={handleSubmit}
              isLoading={isLoading}
              loadingText="저장 중..."
            >
              저장
            </Button>
            <Button onClick={handleClose}>취소</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

export default CategoryModal;