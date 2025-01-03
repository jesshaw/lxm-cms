/** @type {import('tailwindcss').Config} */
module.exports = {
  corePlugins: {
    preflight: false,
  },
  content: ['./src/main/webapp/**/*.{html,js,ts,jsx,tsx,mdx}', './node_modules/primereact/**/*.{html,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        'lxm-primary': 'var(--primary-color)',
        'lxm-surface': 'var(--surface-ground)',
        'lxm-surface-border': 'var(--surface-border)',
        'lxm-surface-card': 'var(--surface-card)',
        'lxm-surface-border': 'var(--surface-border)',
        'lxm-surface-hover': 'var(--surface-hover)',
        'lxm-highlight-bg': 'var(--highlight-bg)',
        'lxm-text': 'var(--text-color)',
        'lxm-text-secondary': 'var(--text-color-secondary)',
      },
      animation: {
        flash: 'flash 0.5s ease-in-out',
        'zoom-in': 'zoom-in 0.5s ease-out',
        'fade-in-right': 'fade-in-right 1s ease-in-out',
        'fade-in': 'fade-in 1s ease-in-out forwards', // forwards ensures it stays at 100% opacity
        'fade-out': 'fade-out 1s ease-in-out forwards', // forwards ensures it stays at 0% opacity
      },
      keyframes: {
        flash: {
          '0%,100%': { opacity: '1' },
          '50%': { opacity: '0' },
        },
        'zoom-in': {
          '0%': { transform: 'scale(0)' },
          '100%': { transform: 'scale(1)' },
        },
        'fade-in-right': {
          '0%': { transform: 'translateX(100%)', opacity: '0' },
          '100%': { transform: 'translateX(0)', opacity: '1' },
        },
        'fade-in': {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        'fade-out': {
          '0%': { opacity: '1' },
          '100%': { opacity: '0' },
        },
      },
    },
  },
  plugins: [],
};
