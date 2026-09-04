const {defineStore}=Pinia

const useMypageStore=defineStore('mypage',{
	state:()=>({
		member_id:0,
		showProfileForm:false,
		vo:{},
		eCount:0,
		formData:{
			u_email:'',
			u_password:'',
			u_passwordConfirm: '', 
			u_name:'',
			u_sex:'',
			u_birthdate:'',
			u_phone:'',
			u_post:'',
			u_addr1:'',
			u_addr2:'',
			u_profile_desc:''
		}
	}),
	actions:{
		async profileData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/profile_update',{
				params:{
					member_id:this.member_id
				}
			})
			console.log(res.data)
			this.eCount=res.data.eCount
			this.vo=res.data.vo
			this.formData.u_name=this.vo.name
			this.formData.u_sex=this.vo.sex
			this.formData.u_birthdate=this.vo.dbBday
			this.formData.u_phone=this.vo.phone
			this.formData.u_post=this.vo.post
			this.formData.u_addr1=this.vo.addr1
			this.formData.u_addr2=this.vo.addr2
			this.formData.u_profile_desc=this.vo.profile_desc
		},
		toggleProfileForm(){
			this.showProfileForm=!this.showProfileForm
		},
		submitForm() {
		    const { u_password, u_passwordConfirm } = this.formData;
		    
		    // 1. 비밀번호 8자 이상 검사
		    if (u_password.length < 8) {
		        alert("비밀번호 숫자 8자 이상으로 입력해 주세요.");
		        return; 
		    }
		    
		    // 2. 비밀번호 일치 검사
		    if (u_password !== u_passwordConfirm) {
		        alert("입력한 비밀번호와 일치하지 않습니다.");
		        return;
		    }
		    
		    // 3. 검증 통과 시 서버로 데이터 전송 
		    alert("프로필 수정이 완료되었습니다.");
		    document.getElementById('profileForm').submit();
		},
		openPostcode() {
			new daum.Postcode({
				oncomplete: (data) => {
					let addr = ''; 
				    if (data.userSelectedType === 'R') { 
				    	addr = data.roadAddress; 
				    }
					else { 
				    	addr = data.jibunAddress; 
				    }
				    // 우편번호와 기본주소 자동 맵핑
				    this.formData.u_post = data.zonecode;
				    this.formData.u_addr1 = addr;
				    // 자동 입력 완료 후 상세주소 입력창으로 포커스 이동
				    document.getElementById('address2').focus();
				}
			}).open();
		}
	}
})