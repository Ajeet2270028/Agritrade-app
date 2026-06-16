import React from 'react'

const NavBar = () => {
  return (
    <>
    <nav className='h-full flex justify-between items-center bg-green-500 text-white p-4'>
        <div className='flex items-center gap-4'>
            <h1>Menu</h1>
            <h1>Logo</h1>
            <h1>Agritrade</h1>
        </div>
        <div className='flex items-center gap-4'>
            <ul className='flex items-center gap-4'>
                <li>Home</li>
                <li>About</li>
                <li>Contact</li>
            </ul>
        </div>
    </nav>
      
    </>
  )
}

export default NavBar
