import React from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { Button } from './FormGroup'
import CookieHelper from '../utils/CookieHelper'

export default function Home({user}) {
  const router = useNavigate()

  if(user == null){
    return <Navigate to={'/login'} />
  }

  const logout = () => {
    CookieHelper.removeCookie('_token')
    router("/login")
  }

  return (
    <div>
        <h1 style={{textAlign: 'center'}}>Welcome to VivaTech</h1>
        <p style={{textAlign: 'center'}}>Job Interview Assignments for VivaTech R&D - The Future of Innovation</p>
        
        <pre style={{width: '50%', whiteSpace: 'normal', margin: 'auto', background: 'black', color: 'white', padding: 20, borderRadius: 5, marginTop: 20}}>
          <h3 style={{color: '#00ea00'}}>User Profile Data :</h3>
          <br/>
          {JSON.stringify(user, null, 2)}
        </pre>
        <br/>
        <center>
          <Button onClick={logout} style={{maxWidth: 200}}>Logout</Button>
        </center>
    </div>
  )
}
