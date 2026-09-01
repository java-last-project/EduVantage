// src/main/resources/static/vue/memberStore.js
const { defineStore } = Pinia;

const useMemberStore = defineStore('member', {
    state: () => ({
        formData: {
            username: '',      
            password: '',      
            passwordConfirm: '', 
            name: '',          
            birthdate: '',    
            sex: 'M',         
            phone: '',
            post: '',         
            addr1: '',         
            addr2: '',         
            profile_desc: ''    
        },
        isIdChecked: false 
    }),
    
    actions: {
        async checkId() {
            if (!this.formData.username) {
                alert("아이디를 입력해 주세요.");
                return;
            }
            
            try {
                const response = await axios.get(`/member/id_check?username=${this.formData.username}`);
                
                if (response.data === 0) {
                    alert("사용 가능한 아이디입니다.");
                    this.isIdChecked = true;
                } else {
                    alert("이미 사용 중인 아이디입니다.");
                    this.isIdChecked = false;
                }
            } catch (error) {
                console.error("중복확인 오류:", error);
            }
        },
        
        submitForm() {
            const { password, passwordConfirm } = this.formData;
            
            // 1. 비밀번호 8자 이상 검사
            if (password.length < 8) {
                alert("비밀번호 숫자 8자 이상으로 입력해 주세요.");
                return; 
            }
            
            // 2. 비밀번호 일치 검사
            if (password !== passwordConfirm) {
                alert("위 입력한 비밀번호와 일치하게 입력해 주세요.");
                return;
            }

            // 3. 아이디 중복 검사 여부 확인
            if (!this.isIdChecked) {
                alert("아이디 중복확인을 진행해 주세요.");
                return;
            }
            
            // 4. 검증 통과 시 서버로 데이터 전송 
            alert("회원가입이 완료되었습니다.");
            document.getElementById('joinForm').submit();
        },
		
		openPostcode() {
		        new daum.Postcode({
		            oncomplete: (data) => {
		                // 사용자가 선택한 주소 타입에 따라 값을 가져오기
		                let addr = ''; 
		                if (data.userSelectedType === 'R') { 
		                    addr = data.roadAddress; 
		                } else { 
		                    addr = data.jibunAddress; 
		                }

		                // 우편번호와 기본주소 자동 맵핑
		                this.formData.post = data.zonecode;
		                this.formData.addr1 = addr;

		                // 자동 입력 완료 후 상세주소 입력창으로 포커스 이동
		                document.getElementById('address2').focus();
		            }
		        }).open();
		    }
    }
});