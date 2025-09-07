const withNextra = require('nextra')({
  theme: 'nextra-theme-docs',
  themeConfig: './theme.config.jsx'
})

module.exports = withNextra({
  // 本地開發時不使用 basePath
  ...(process.env.NODE_ENV === 'production' && {
    output: 'export',
    trailingSlash: true,
    basePath: '/cloudy_homework',
    assetPrefix: '/cloudy_homework'
  }),
  images: {
    unoptimized: true
  }
})