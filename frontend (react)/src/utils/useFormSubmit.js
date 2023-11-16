import { useCallback, useState } from 'react'

export default function useFormSubmit(callback, onErrorCallBack, dependencies = []) {
  const [isLoading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const onSubmit = useCallback(async (event) => {
    event.preventDefault();

    if(isLoading == false) {
        setLoading(true)
        try{
            await callback(event)
            setError(null)
        }
        catch(e){
            setError(e)
            if(typeof onErrorCallBack == 'function'){
              onErrorCallBack(e)
            }
        }
        finally{
            setLoading(false)
        }
    }
    
  }, [isLoading, callback, ...dependencies])

  return [onSubmit, isLoading, error]
}
