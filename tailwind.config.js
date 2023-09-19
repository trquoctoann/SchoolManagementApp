/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    'src/main/resources/templates/*.html',
    'src/main/resources/templates/account/*.html',
    'src/main/resources/templates/education/*.html',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        nunito: ['Nunito', 'sans-serif'],
      },
      colors: {
        teal: '#14b8a6',
        midnight: '#0f172a',
      },
      screens: {
        xs: '480px',
      },
    },
  },
  plugins: [],
};
