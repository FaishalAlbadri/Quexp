package com.bintang.quexp.ui.fragment.ar

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.R
import com.bintang.quexp.adapter.ARAdapter
import com.bintang.quexp.data.local.ARData
import com.bintang.quexp.databinding.FragmentAugmentedRealityBinding
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.concurrent.CompletableFuture

class AugmentedRealityFragment : Fragment() {

    private var _binding: FragmentAugmentedRealityBinding? = null
    val binding get() = _binding!!

    private val viewModel by viewModels<AugmentedRealityViewModel>()
    private lateinit var arAdapter: ARAdapter
    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene

    private lateinit var model: CompletableFuture<ModelRenderable>
    private var modelRenderable: Renderable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAugmentedRealityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arFragment = (childFragmentManager.findFragmentById(R.id.fragment_ar) as ArFragment).apply {
            setOnSessionConfigurationListener { session, config ->
            }
            setOnViewCreatedListener { arSceneView ->
                arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL)
            }
            setOnTapArPlaneListener(::onTapPlane)
        }

        viewModel.apply {
            arData.observe(viewLifecycleOwner) {
                setArList(it)
                loadModels(it[0].ar)
            }
            getArData()
        }

    }

    private fun setArList(it: List<ARData>) {
        arAdapter = ARAdapter(it) { data ->
            scene.callOnHierarchy { node ->
                if (node is AnchorNode){
                    node.anchor!!.detach()
                }
            }
            loadModels(data.ar)
            arAdapter.apply {
                selectedItem = data.position
                notifyDataSetChanged()
            }
        }
        binding.rvAr.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager
                    .HORIZONTAL, false
            )
            adapter = arAdapter
        }
    }

    private fun loadModels(source: String) {
        model = ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(source))
            .setIsFilamentGltf(true)
            .build()

        model.thenAccept { renderableObject -> modelRenderable = renderableObject }
        model.exceptionally {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            null
        }

    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        if (modelRenderable == null) {
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
            return
        }

        scene.addChild(AnchorNode(hitResult.createAnchor()).apply {
            addChild(TransformableNode(arFragment.transformationSystem).apply {
                localScale = Vector3(0.1f, 0.1f, 0.1f)
                renderable = modelRenderable
                renderableInstance.setCulling(false)
                renderableInstance.animate(true).start()
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}