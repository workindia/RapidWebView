/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
}

const withAssetsManifest = require('next-assets-manifest');
const packageJson = require('./package.json')

PUBLIC_PATH = 'https://rapid-web-view.netlify.app/_next/';

module.exports = nextConfig

module.exports = withAssetsManifest({
  assetsManifest: {
    // https://github.com/webdeveric/webpack-assets-manifest#options-read-the-schema
    output: 'static/assets-manifest.json',
    transform(assets, manifest) {
      const items = Object(null)
      const processedAssets = [];

      Object.keys(assets).forEach(key => {
        if (!assets[key].includes("json")) {
          processedAssets.push((`${PUBLIC_PATH}${assets[key]}`));
        }
      })

      items['version'] = packageJson.version
      items['asset_urls'] = processedAssets;

      return items;
    },
  },
});
