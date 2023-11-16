import React, { useMemo, useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import Status from './Status';
import { Button } from './FormGroup';
import { IoCheckmarkDone, IoChevronBack } from 'react-icons/io5';
import useFormSubmit from '../utils/useFormSubmit';
import axios from '../utils/Axios';

function InitialPage({sendOtp, statusData, email, loading}){
    return (
      <form onSubmit={sendOtp} className='form'>
          <h3 className='title' style={{color: 'gray'}}>Verify Your Account</h3>
          <p className='description'>
          Thank you for registering with our service. To complete the registration process, please click on the verification button below.
          </p>
          <b className='description' style={{marginTop: 0}}>
              {email}
          </b>
          <Status {...statusData} />
          <Button isLoading={loading}>
            Verify
          </Button>
          <p className='description'>
              If you won't receive any code, you can contact us.
          </p>
      </form>
    )
}

function OtpPage({sendOtp, verifyOtp, email, timer, statusData, sentSuccess, loading}){

return (
    <form onSubmit={verifyOtp} className='form'>
            <h3 className='title' style={{color: 'gray'}}>Verify Your Account</h3>
            <p className='description'>
            Thank you for registering with our service.
            {
            sentSuccess ? " OTP has been sent to your email address ":
                                " OTP has been already sent to your email address "
                            
            }
            <i>{email}</i>.
            </p>
            <input type='text' placeholder='OTP' name='otp' />
            <div className='register' style={{fontSize: 13, padding: 0, alignSelf: 'flex-end', cursor: 'pointer'}}>
            <a onClick={sendOtp}>Resend OTP {timer ? `(${timer}s)`:''}</a>
            </div>
            <Status {...statusData} />
            <Button isLoading={loading}>
            Verify
            </Button>
        </form>
    )
}
function VerifiedPage(){
    return (
        <form className='form'>
            <center>
            <IoCheckmarkDone style={{fontSize: 40, color: 'var(--theme-color)'}}/>
            </center>
            <h3 className='title' style={{color: 'var(--theme-color)'}}>Account Verified</h3>
            <p className='description'>
                Thank you for registering with our service. Your account is now verified, you can use your username and password for login.
            </p>
            <Link to='/'>
                <Button>
                <IoChevronBack style={{marginRight: 10}}/> Go Back To Home
                </Button>
            </Link>
        </form>
    )
}
  
export default function Verify() {
    const router = useNavigate();
    const [email, setEmail] = useState("")
    const [statusData, setStatusData] = useState({})
    const [pageIndex, setPageIndex] = useState(0)
    const [timer, setTimer] = useState(0)
    const [otpSentSuccess, setOtpSentSuccess]  = useState(true)

    const emailText = useMemo(() => {
        let [username, postfix] = email.split('@')
        let text = username.slice(0, Math.floor(username.length/2));
        text += Array(Math.floor(username.length/2)).fill('*').join('') 
        text += postfix
        return text
    }, [email])

    useEffect(() => {
        const userEmail = sessionStorage.getItem('verification-email')
        if(!userEmail || !userEmail.includes('@')) router("/login")
        else setEmail(userEmail)
    }, [])

    const finalCallback = () => {
      setPageIndex(1)
      let interval = setInterval(() => {
        setTimer(prev => {
          if(prev <= 1) clearInterval(interval)
          return Math.max(prev - 1, 0)
        })
      }, 1000)
    }
    const callback = async () => {
      if(timer != 0) return;
      await axios.get(`/user/send-otp?email=${email}`)
      setTimer(30)
      setOtpSentSuccess(true)
      finalCallback()
      setStatusData({})
    }
    const errorCallBack = (e) => {
     if(e.response.status == 429){
        let retryAfter = e.response.headers.get("Retry-After")
        if(retryAfter) retryAfter = Date.parse(retryAfter)
        else retryAfter = Date.now() + (1000 * 30)
        let dateDiffInSec = Math.floor((retryAfter - Date.now()) / 1000)
        setTimer(dateDiffInSec)
        setStatusData({})
     }
     else{
       setStatusData({msg: e.response?.data?.message || e?.message, isError: true})
     }
     setOtpSentSuccess(false)
     finalCallback()
    }

    // send-otp handler
    const [onSubmit, isLoading] = useFormSubmit(
      callback,
      errorCallBack,
      [timer, email]
    )

    // verify otp handler
    const [verifyOtp, isVerifying] = useFormSubmit(async (e) => {
      let otp = e.target.otp.value
      await axios.get(`/user/verify?username=${email}&otp=${otp}`)
      setStatusData({})
      setPageIndex(2)
      sessionStorage.removeItem('verification-email')
    },
    (e) => {
      setStatusData({msg: e.response?.data?.message || e?.message, isError: true})
    },
    [timer, email])
  
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
                {
                  [
                    <InitialPage 
                      sendOtp={onSubmit} 
                      email={emailText}
                      loading={isLoading}
                      statusData={statusData}
                    />,
                    <OtpPage 
                      sendOtp={onSubmit}
                      verifyOtp={verifyOtp}
                      email={emailText}
                      timer={timer}
                      statusData={statusData}
                      sentSuccess={otpSentSuccess}
                      loading={isVerifying}
                    />,
                    <VerifiedPage />
                  ][pageIndex]
                }
                <hr className='divider' />
                <br/>
                <p className='register'>
                  Already have an Account? <br />
                  <Link to='/login'>login with exisiting account</Link>
                </p>
                <br/>
            </div>
        </div>
  )
}
