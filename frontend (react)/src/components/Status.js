import React from 'react'

export default function Status({msg, isError = false}) {
  if(!msg) return <div></div>
  return (
    <div className={isError ? 'status error':'status success'}>
      {msg}
    </div>
  )
}
