import './App.css';
import { Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import { useEffect, useState } from 'react';
import Register from './components/Register';
import Verify from './components/Verify';
import CookieHelper from './utils/CookieHelper'
import axios from './utils/Axios';

function App() {
  const [user, setUser] = useState(null)
  const [isLoading, setLoading] = useState(true)

  useEffect(() => {
    const authenticate = async () => {
      if(CookieHelper.getCookie('_token')){
        try{
          let response = await axios.get('/user')
          return response.data
        }
        catch(e){
          // invalid or expired token
          CookieHelper.removeCookie('_token')
        }
      }
      return null 
    }
    
    authenticate()
    .then(setUser)
    .finally(() => setLoading(false)) 
  }, [])

  return (
    <div className="App">
      {
        isLoading ? <h3>Loading...</h3>:(
          <Routes>
            <Route path='/' element={<Home user={user}/>} />
            <Route path='/login' element={<Login setUser={setUser}/>} />
            <Route path='/register' element={<Register/>} />
            <Route path='/verify' element={<Verify/>} />
          </Routes>
        )
      }
    </div>
  );
}

export default App;
