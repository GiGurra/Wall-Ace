package se.gigurra.wallace.client.renderer

class RenderAssets {
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 40)
  val libgdxLogo = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
  val mapSprite = Sprite.fromSize(256, 256, useMipMaps = true)
}
