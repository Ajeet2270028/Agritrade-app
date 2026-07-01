import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import NavBar from './components/NavBar'
import SideBar from './components/SideBar'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Dashboard from './components/Dashboard'
import PurchaseGrain from './components/PurchaseGrain'
import Farmer from './components/Farmer'
import CreateFarmer from './components/CreateFarmer'

function App() {
  return (
    <>
      <NavBar />

      <div className="flex">
        <SideBar />

        <div className="flex-1 p-4">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/purchase" element={<PurchaseGrain />} />
            <Route path="/farmer" element={<Farmer />} />
            <Route path="/farmer/create-farmer" element={<CreateFarmer/>} />
            <Route path="/farmer/edit-farmer/:id" element={<CreateFarmer />} />
          </Routes>
        </div>
      </div>
    </>
  )
}
export default App
