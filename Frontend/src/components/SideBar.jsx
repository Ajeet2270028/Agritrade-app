import React from 'react'

const SideBar = () => {
  return (
    <>
     <aside className='w-40 h-screen bg-green-500 p-4'>
        <nav className='h-full flex flex-col justify-start items-start gap-4'>
            <ul className='flex flex-col items-start gap-4 text-white'>
                <li>Home</li>
                <li>About</li>
                <li>Contact</li>
            </ul>
        </nav>
     </aside>
      
    </>
  )
}

export default SideBar
