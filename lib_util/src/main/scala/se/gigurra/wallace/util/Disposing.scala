package se.gigurra.wallace.util

trait Disposing[ResourceType] {
  def dispose(r: ResourceType): Unit
}
