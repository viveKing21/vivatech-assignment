import React, {useEffect, useState} from 'react'
import { Link, useNavigate } from 'react-router-dom'
import Status from './Status'
import axios from '../utils/Axios'
import { InputPassword } from './FormGroup'

export default function Register() {
    const router = useNavigate()
    const [statusData, setStatusData] = useState({msg: ''})
  
    useEffect(() => {
      
    }, [])
    
    const onSubmit = async (e) => {
      e.preventDefault()
  
      let formData = new FormData(e.target)
  
      const data = {
        username: formData.get("username"),
        password: formData.get("password"),
        userProfile: {
          name: formData.get("name"),
          email: formData.get("email"),
          dob: formData.get("dob"),
          gender: formData.get("gender")
        }
      }
  
      try{
        let res = await axios.post('/user/register', data)
  
        if(res.data.status == 'INACTIVE'){
          sessionStorage.setItem('verification-email', data.userProfile.email)
          router("/verify")
        }
        else{
          setStatusData({msg: "Account created successfully, now you can login"})
        }
      }
      catch(e){
        let msg = e.response.data.message
        setStatusData({msg, isError: true})
      }
    }
    return (
        <div className='login-container'>
              <div className='container'>
                  <div className='logo'>
                      <img
                          src='https://vivatechrnd.com/wp-content/uploads/2021/09/Screenshot-35-1.png'
                          alt='Image not found'
                          width={40}
                          height={40}
                      />
                  </div>
                  <h3 className='title'>VivaTech</h3>
                  <p className='description'>CRBT or Caller Ring back tone solution, IVR or Interactive Voice Response, Call completion.</p>
                  <form onSubmit={onSubmit}>
                    <input type='text' name='name' placeholder='Full Name' />
                    <input type='text' name='username' placeholder='Username' />
                    <input type='email' name='email' placeholder='Email' />
                    <InputPassword type='password' name='password' placeholder='Password' />
                    <input type='date' name='dob' placeholder='dob' />
                    <select name='gender'>
                        <option value=''>Gender</option>
                        <option value='MALE'>Male</option>
                        <option value='FEMALE'>Female</option>
                        <option value='OTHER'>Other</option>
                    </select>
                    <Status {...statusData} />
                    <input type='submit' value='Register' />
                </form>
                <hr className='divider' />
                <br />
                <p className='register'>
                    Already have an Account? <br />
                    <Link to='/login'>login with exisiting account</Link>
                </p>
                <br />
            </div>
        </div>
    )
}
