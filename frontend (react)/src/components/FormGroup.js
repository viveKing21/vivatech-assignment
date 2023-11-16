import React, { useState } from 'react'
import { IoEye, IoEyeOff } from 'react-icons/io5'

export default function Input({className, icon, ...props}) {
  return (
    <label className={className} custom-input=''>
        <input {...props} className='default'/>
        {icon}
    </label>
  )
}

function InputPassword(props){
  const [show, setShow] = useState(false)

  return <Input 
          {...props} 
          type={show ? "text":"password"} 
          icon={
            <div className='icon' onClick={()=>setShow(prev => !prev)}>
              {!show ? <IoEye/>:<IoEyeOff/>}
            </div>
          }
        />
}

function Button({isLoading, children, onClick, ...props}){
  return <button {...props} onClick={isLoading ? undefined:onClick}>
    {
      isLoading ? 
      <div className='spinner'></div>:children
    }
  </button>
}


export {Input,  InputPassword, Button}