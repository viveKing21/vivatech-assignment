import React, {useState, useEffect} from 'react'
import axios from '../utils/Axios'
import useFormSubmit from '../utils/useFormSubmit'
import Status from '../components/Status'
import {Button, InputPassword} from './FormGroup'
import { Link, useNavigate } from 'react-router-dom'
import CookieHelper from '../utils/CookieHelper'

export default function Login({setUser}) {
    const router = useNavigate()
    const [statusData, setStatusData] = useState({})
  
    const [onSubmit, isLoading, error] = useFormSubmit(async e => {
        let formData = new FormData(e.target)
        let data = {
          username: formData.get("username"),
          password: formData.get("password")
        }
        let res = await axios.post("/user/login", data, {
          headers: {
            'Save-Token': "_token",
            'Remember-Token': formData.get('remember-me') ? 'true':'false',
            'Path-Token': '/'
          }
        })
        
        if(res.data.status == 'ACTIVE'){
          let { data: user } = await axios.get('/user', {
            headers: {'Authorization': 'Bearer ' + CookieHelper.getCookie('_token')}
          })

          setUser(user)
          setStatusData({msg: "Login successfully (Redirecting...)"})
          setTimeout(() => {
              router("/")
          }, [3000])
        }
        else if(res.data.status == 'INACTIVE'){
          sessionStorage.setItem('verification-email', res.data.email)
          router("/verify")
        }
        else throw "Account is not active"
    }, e => setStatusData({msg: e.response.data.message, isError: true}))
  
    useEffect(() => {
      
    }, [])
  
  
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
                      <input type='text' name='username' placeholder='Username or Email' />
                      <InputPassword placeholder='Password' name='password'/>
                      <label className='checkbox' style={{margin: 0}}>
                          <input type='checkbox' name='remember-me'/>
                          <div></div>
                          Remember Me
                      </label>
                      <Status {...statusData}/>
                      <Button type='submit' isLoading={isLoading}>Login</Button>
                      <Link href='/reset' className='reset'>
                          Forgot Password?
                      </Link>
                  </form>
                  <hr className='divider' />
                  <br/>
                  <p className='register'>
                    Don't have an account? <br />
                    <Link to='/register'>Create a new Account</Link>
                  </p>
                  <br/>
              </div>
          </div>
    )
  }