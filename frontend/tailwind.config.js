/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        combi: {
          ink: '#121826',
          blue: '#005BBB',
          cyan: '#00A3E0',
          orange: '#FF8A00',
          green: '#0EAD69'
        }
      },
      boxShadow: {
        soft: '0 18px 45px rgba(15, 23, 42, 0.10)'
      }
    }
  },
  plugins: []
};
